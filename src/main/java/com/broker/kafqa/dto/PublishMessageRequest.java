package com.broker.kafqa.dto;

import jakarta.validation.constraints.NotBlank;

public class PublishMessageRequest {

    @NotBlank(message = "payload must not be blank")
    private String payload;

    public PublishMessageRequest() { }

    public PublishMessageRequest(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
