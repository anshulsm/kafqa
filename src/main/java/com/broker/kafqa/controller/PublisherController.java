package com.broker.kafqa.controller;

import com.broker.kafqa.dto.PublishMessageRequest;
import com.broker.kafqa.service.BrokerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/topics/{topicName}/publish")
public class PublisherController {

    private final BrokerService brokerService;

    public PublisherController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostMapping
    public ResponseEntity<Void> publish(
            @PathVariable String topicName,
            @Valid @RequestBody PublishMessageRequest req) {
        brokerService.publish(topicName, req.getPayload());
        return ResponseEntity.ok().build();
    }
}
