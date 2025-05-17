package com.broker.kafqa.model;

import java.time.Instant;

public class DeadLetter {
    private final Message originalMessage;
    private final String failureReason;
    private final Instant failedAt;

    public DeadLetter(Message originalMessage, String failureReason) {
        this.originalMessage = originalMessage;
        this.failureReason = failureReason;
        this.failedAt = Instant.now();
    }

    public Message getOriginalMessage() {
        return originalMessage;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Instant getFailedAt() {
        return failedAt;
    }
}
