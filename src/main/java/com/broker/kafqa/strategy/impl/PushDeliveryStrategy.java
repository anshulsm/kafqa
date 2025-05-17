package com.broker.kafqa.strategy.impl;

import com.broker.kafqa.model.Message;
import com.broker.kafqa.model.Subscription;
import com.broker.kafqa.strategy.DeliveryStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PushDeliveryStrategy implements DeliveryStrategy {

    private final RestTemplate restTemplate;

    public PushDeliveryStrategy() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Deliver the message by HTTP POST to the subscriber's callbackUrl.
     *
     * @param subscription contains the subscriber info (including callbackUrl)
     * @param message      the Message to deliver
     * @throws Exception if the HTTP call fails or returns non-2xx
     */
    @Override
    public void deliver(Subscription subscription, Message message) throws Exception {
        String callbackUrl = subscription.getSubscriber().getCallbackUrl();
        if (callbackUrl == null || callbackUrl.isEmpty()) {
            throw new IllegalArgumentException("Callback URL is not configured for PUSH subscriber: "
                    + subscription.getSubscriber().getId());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Message> entity = new HttpEntity<>(message, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(callbackUrl, entity, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(
                        "Failed to push message to subscriber at " + callbackUrl +
                                ". HTTP status: " + response.getStatusCode());
            }
        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "Error while delivering message to subscriber at " + callbackUrl, ex);
        }
    }
}
