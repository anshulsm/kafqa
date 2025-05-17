package com.broker.kafqa.service.impl;

import com.broker.kafqa.exception.SubscriptionNotFoundException;
import com.broker.kafqa.exception.TopicNotFoundException;
import com.broker.kafqa.model.*;
import com.broker.kafqa.repository.SubscriptionRepository;
import com.broker.kafqa.repository.TopicRepository;
import com.broker.kafqa.service.BrokerService;
import com.broker.kafqa.strategy.impl.PullDeliveryStrategy;
import com.broker.kafqa.strategy.impl.PushDeliveryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BrokerServiceImpl implements BrokerService {

    private static final Logger logger = LoggerFactory.getLogger(BrokerServiceImpl.class);


    private final TopicRepository topicRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final PushDeliveryStrategy pushStrategy;
    private final PullDeliveryStrategy pullStrategy;
    private final int maxRetries;

    // simple in-memory DLQ
    private final List<DeadLetter> deadLetters = new ArrayList<>();

    public BrokerServiceImpl(TopicRepository topicRepo,
                             SubscriptionRepository subscriptionRepo,
                             PushDeliveryStrategy pushStrategy,
                             PullDeliveryStrategy pullStrategy,
                             @Value("${broker.maxRetries:3}") int maxRetries) {
        this.topicRepo = topicRepo;
        this.subscriptionRepo = subscriptionRepo;
        this.pushStrategy = pushStrategy;
        this.pullStrategy = pullStrategy;
        this.maxRetries = maxRetries;
    }

    @Override
    public void createTopic(String topicName) {
        topicRepo.createTopic(topicName);
        logger.info("Created topic: {}", topicName);
    }

    @Override
    public void deleteTopic(String topicName) {
        logger.info("Deleting topic: {}", topicName);
        topicRepo.deleteTopic(topicName);
    }

    @Override
    public void publish(String topicName, String payload) {
        logger.info("Publishing message to topic {}: payload={}", topicName, payload);
        Topic topic = topicRepo.findByName(topicName);
        if (topic == null) {
            logger.error("Publish failed – topic not found: {}", topicName);
            throw new TopicNotFoundException(topicName);
        }

        Message message = new Message(topicName, payload);
        topic.enqueue(message);

        for(Subscription sub: topic.getSubscriptions()) {
            DeliveryMode mode = sub.getSubscriber().getMode();
            logger.debug("Delivering message {} to subscription {} with mode {}", message.getId(), sub.getId(), mode);
            try {
                if (mode == DeliveryMode.PUSH) {
                    pushStrategy.deliver(sub, message);
                } else if (mode == DeliveryMode.PULL) {
                    pullStrategy.deliver(sub, message);
                }
                logger.info("Delivered message {} to subscription {}", message.getId(), sub.getId());
            } catch (Exception e) {
                logger.warn("Delivery failed for message {} on subscription {}: {}",
                        message.getId(), sub.getId(), e.getMessage());
                // Handle delivery failure
                sub.incrementRetry();
                if(sub.getRetryCount() > maxRetries) {
                    logger.error("Max retries exceeded for message {} on subscription {}, moving to dead letters",
                            message.getId(), sub.getId());
                    deadLetters.add(new DeadLetter(message, e.getMessage()));
                }
            }
        }
    }

    @Override
    public UUID subscribe(String topicName, DeliveryMode mode, String callbackUrl) {
        logger.info("Subscribing to topic {}: mode={}, callbackUrl={}", topicName, mode, callbackUrl);
        Topic topic = topicRepo.findByName(topicName);
        if (topic == null) {
            logger.error("Subscribe failed – topic not found: {}", topicName);
            throw new TopicNotFoundException(topicName);
        }

        Subscriber subscriber = new Subscriber(callbackUrl, mode);
        Subscription sub = subscriptionRepo.add(topic, subscriber);
        logger.info("Created subscription {} for topic {}", sub.getId(), topicName);
        return sub.getId();
    }

    @Override
    public List<Message> consume(String topicName, UUID subscriptionId) {
        logger.info("Consuming messages for topic {}, subscription {}", topicName, subscriptionId);
        Subscription subscription = subscriptionRepo.findById(subscriptionId);
        if (subscription == null) {
            logger.error("Consume failed – subscription not found: {}", subscriptionId);
            throw new SubscriptionNotFoundException(subscriptionId);
        }

        if (subscription.getSubscriber().getMode() != DeliveryMode.PULL) {
            logger.error("Consume failed – subscription {} is not in PULL mode", subscriptionId);
            throw new IllegalStateException("Subscription is not PULL mode: "+subscriptionId);
        }

        Topic topic = topicRepo.findByName(topicName);
        if (topic == null) {
            logger.error("Consume failed – topic not found: {}", topicName);
            throw new TopicNotFoundException(topicName);
        }

        List<Message> all = topic.getMessages();
        long offset = subscription.getOffset();

        if (offset >= all.size()) {
            logger.info("No new messages for subscription {} (offset={}, totalMessages={})",
                    subscriptionId, offset, all.size());
            return List.of();
        }

        List<Message> pending = all.subList((int) offset, all.size());
        subscription.setOffset(all.size());

        logger.info("Returning {} messages for subscription {} and updating offset to {}",
                pending.size(), subscriptionId, all.size());
        return pending;
    }

    @Override
    public void ack(String topicName, UUID subscriptionId, UUID messageId) {
        logger.info("Acknowledging message {} for subscription {} on topic {}", messageId, subscriptionId, topicName);
        Subscription subscription = subscriptionRepo.findById(subscriptionId);
        if (subscription == null) {
            logger.error("Ack failed – subscription not found: {}", subscriptionId);
            throw new SubscriptionNotFoundException(subscriptionId);
        }

        Topic topic = topicRepo.findByName(topicName);
        if (topic == null) {
            logger.error("Ack failed – topic not found: {}", topicName);
            throw new TopicNotFoundException(topicName);
        }

        List<Message> all = topic.getMessages();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(messageId)) {
                subscription.setOffset(i + 1);
                logger.info("Offset for subscription {} updated to {}", subscriptionId, i + 1);
                return;
            }
        }

        logger.error("Ack failed – message ID {} not found on topic {}", messageId, topicName);
        throw new IllegalStateException("Message ID not found: "+messageId);
    }
}
