package com.broker.kafqa.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AckRequest {

    @NotNull(message = "subscriptionId must not be null")
    private UUID subscriptionId;

    @NotNull(message = "messageId must not be null")
    private UUID messageId;

    public AckRequest() { }

    public AckRequest(UUID subscriptionId, UUID messageId) {
        this.subscriptionId = subscriptionId;
        this.messageId = messageId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }
}
