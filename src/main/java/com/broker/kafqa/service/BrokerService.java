package com.broker.kafqa.service;

import com.broker.kafqa.model.DeliveryMode;
import com.broker.kafqa.model.Message;

import java.util.List;
import java.util.UUID;

public interface BrokerService {
    void createTopic(String topicName);
    void deleteTopic(String topicName);

    void publish(String topicName, String payload);

    /**
     * Subscribe to a topic; returns the subscriptionId.
     */
    UUID subscribe(String topicName, DeliveryMode mode, String callbackUrl);

    /**
     * For PULL subscriptions: retrieve all messages since last offset.
     */
    List<Message> consume(String topicName, UUID subscriptionId);

    /**
    * Acknowledge processing of a single message (moves offset past it).
    */
    void ack(String topicName, UUID subscriptionId, UUID messageId);
}
