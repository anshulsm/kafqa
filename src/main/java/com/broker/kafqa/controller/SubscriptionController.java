package com.broker.kafqa.controller;

import com.broker.kafqa.dto.AckRequest;
import com.broker.kafqa.dto.SubscribeRequest;
import com.broker.kafqa.model.Message;
import com.broker.kafqa.service.BrokerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/topics/{topicName}/subscriptions")
public class SubscriptionController {

    private final BrokerService brokerService;

    public SubscriptionController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostMapping
    public ResponseEntity<UUID> subscribe(
            @PathVariable String topicName,
            @Valid @RequestBody SubscribeRequest req) {
        UUID subscriptionId = brokerService.subscribe(
                topicName,
                req.getMode(),
                req.getCallbackUrl()
        );
        return ResponseEntity.ok(subscriptionId);
    }

    @GetMapping("/{subscriptionId}/consume")
    public ResponseEntity<List<Message>> consume(
            @PathVariable String topicName,
            @PathVariable UUID subscriptionId) {
        List<Message> messages = brokerService.consume(topicName, subscriptionId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{subscriptionId}/ack")
    public ResponseEntity<Void> ack(
            @PathVariable String topicName,
            @PathVariable UUID subscriptionId,
            @Valid @RequestBody AckRequest req) {
        brokerService.ack(topicName, subscriptionId, req.getMessageId());
        return ResponseEntity.noContent().build();
    }
}
