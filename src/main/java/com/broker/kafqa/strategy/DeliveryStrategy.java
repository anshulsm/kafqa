package com.broker.kafqa.strategy;

import com.broker.kafqa.model.Message;
import com.broker.kafqa.model.Subscription;

public interface DeliveryStrategy {

    /**
     * Deliver the given message to the subscriber bound by the subscription.
     *
     * @param subscription the Subscription holding subscriber info and state
     * @param message      the Message to deliver
     * @throws Exception   if delivery fails (e.g. HTTP callback fails for PUSH mode)
     */
    void deliver(Subscription subscription, Message message) throws Exception;
}
