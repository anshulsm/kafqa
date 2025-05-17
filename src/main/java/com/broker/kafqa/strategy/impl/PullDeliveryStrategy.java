package com.broker.kafqa.strategy.impl;

import com.broker.kafqa.model.Message;
import com.broker.kafqa.model.Subscription;
import com.broker.kafqa.strategy.DeliveryStrategy;
import org.springframework.stereotype.Service;

@Service
public class PullDeliveryStrategy implements DeliveryStrategy {

    /**
     * In PULL mode, we do not push messages immediately.
     * Messages remain in the topic's queue and will be returned
     * when the subscriber calls the consume endpoint.
     */
    @Override
    public void deliver(Subscription subscription, Message message) {
        // no-op for pull delivery
    }
}
