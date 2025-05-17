package com.broker.kafqa.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTopicRequest {

    @NotBlank(message = "topicName must not be blank")
    private String topicName;

    public CreateTopicRequest() { }

    public CreateTopicRequest(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
