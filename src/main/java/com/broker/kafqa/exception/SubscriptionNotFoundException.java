package com.broker.kafqa.exception;

import java.util.UUID;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(UUID subscriptionId) {
        super("Subscription not found: " + subscriptionId);
    }

    public SubscriptionNotFoundException(UUID subscriptionId, Throwable cause) {
        super("Subscription not found: " + subscriptionId, cause);
    }
}
