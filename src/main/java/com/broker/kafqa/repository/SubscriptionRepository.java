package com.broker.kafqa.repository;

import com.broker.kafqa.model.Subscriber;
import com.broker.kafqa.model.Subscription;
import com.broker.kafqa.model.Topic;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository {
    /**
     * Add a new Subscription for given topic & subscriber.
     * Also registers it on the Topic itself.
     */
    Subscription add(Topic topic, Subscriber subscriber);

    /** Remove subscription (and unregister from its Topic) */
    void remove(UUID subscriptionId);

    /** Find all subscriptions for a given topic */
    List<Subscription> findByTopic(String topicName);

    /** Lookup a subscription by its ID */
    Subscription findById(UUID subscriptionId);
}
