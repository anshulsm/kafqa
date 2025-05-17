package com.broker.kafqa.controller;

import com.broker.kafqa.dto.CreateTopicRequest;
import com.broker.kafqa.service.BrokerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/topics")
public class TopicController {

    private final BrokerService brokerService;

    public TopicController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostMapping
    public ResponseEntity<Void> createTopic(@Valid @RequestBody CreateTopicRequest req) {
        brokerService.createTopic(req.getTopicName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{topicName}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String topicName) {
        brokerService.deleteTopic(topicName);
        return ResponseEntity.noContent().build();
    }

}
