package com.broker.kafqa.repository.impl;

import com.broker.kafqa.model.Topic;
import com.broker.kafqa.repository.TopicRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryTopicRepository implements TopicRepository {

    private final ConcurrentHashMap<String, Topic> topics = new ConcurrentHashMap<>();

    @Override
    public void createTopic(String name) {
        topics.putIfAbsent(name, new Topic(name));
    }

    @Override
    public void deleteTopic(String name) {
        topics.remove(name);
    }

    @Override
    public Topic findByName(String name) {
        return topics.get(name);
    }

    @Override
    public List<Topic> listTopics() {
        return new ArrayList<>(topics.values());
    }
}
