package com.broker.kafqa.exception;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String topic) {
        super("Topic not found: " + topic);
    }

    public TopicNotFoundException(String topic, Throwable cause) {
        super("Topic not found: " + topic, cause);
    }
}
