package com.broker.kafqa.model;

import java.util.UUID;

public class Subscriber {
    private final UUID id;
    private final String callbackUrl;
    private final DeliveryMode mode;

    public Subscriber(String callbackUrl, DeliveryMode mode) {
        this.id = UUID.randomUUID();
        this.callbackUrl = callbackUrl;
        this.mode = mode;
    }

    public UUID getId() {
        return id;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public DeliveryMode getMode() {
        return mode;
    }
}
