package com.broker.kafqa.model;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final String topicName;
    private final String payload;
    private final Instant timestamp;

    public Message(String topicName, String payload) {
        this.id = UUID.randomUUID();
        this.topicName = topicName;
        this.payload = payload;
        this.timestamp = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
