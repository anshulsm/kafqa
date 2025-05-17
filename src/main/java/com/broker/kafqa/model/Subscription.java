package com.broker.kafqa.model;

import java.util.UUID;

public class Subscription {
    private final UUID id;
    private final Topic topic;
    private final Subscriber subscriber;
    private long offset;
    private int retryCount;

    public Subscription(Topic topic, Subscriber subscriber) {
        this.id = UUID.randomUUID();
        this.topic = topic;
        this.subscriber = subscriber;
        this.offset = 0L;
        this.retryCount = 0;
    }

    public UUID getId() {
        return id;
    }

    public Topic getTopic() {
        return topic;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public long getOffset() {
        return offset;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void resetRetry() {
        this.retryCount = 0;
    }

    public void incrementRetry() {
        this.retryCount++;
    }
}
