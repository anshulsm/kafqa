package com.broker.kafqa.dto;

import com.broker.kafqa.model.DeliveryMode;
import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {

    @NotNull(message = "mode must not be null")
    private DeliveryMode mode;

    /**
     * Required when mode == PUSH; ignored otherwise.
     */
    private String callbackUrl;

    public SubscribeRequest() { }

    public SubscribeRequest(DeliveryMode mode, String callbackUrl) {
        this.mode = mode;
        this.callbackUrl = callbackUrl;
    }

    public DeliveryMode getMode() {
        return mode;
    }

    public void setMode(DeliveryMode mode) {
        this.mode = mode;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
