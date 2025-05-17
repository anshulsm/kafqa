package com.broker.kafqa.repository.impl;

import com.broker.kafqa.model.Subscriber;
import com.broker.kafqa.model.Subscription;
import com.broker.kafqa.model.Topic;
import com.broker.kafqa.repository.SubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemorySubscriptionRepository implements SubscriptionRepository {

    private final ConcurrentHashMap<UUID, Subscription> subscriptions = new ConcurrentHashMap<>();

    @Override
    public Subscription add(Topic topic, Subscriber subscriber) {
        Subscription sub = new Subscription(topic, subscriber);
        subscriptions.put(sub.getId(), sub);
        topic.addSubscription(sub);
        return sub;
    }

    @Override
    public void remove(UUID subscriptionId) {
        Subscription sub = subscriptions.remove(subscriptionId);
        if (sub != null) {
            sub.getTopic().removeSubscription(subscriptionId);
        }
    }

    @Override
    public List<Subscription> findByTopic(String topicName) {
        return subscriptions.values().stream()
                .filter(sub -> sub.getTopic().getName().equals(topicName))
                .collect(Collectors.toList());
    }

    @Override
    public Subscription findById(UUID subscriptionId) {
        return subscriptions.get(subscriptionId);
    }
}
