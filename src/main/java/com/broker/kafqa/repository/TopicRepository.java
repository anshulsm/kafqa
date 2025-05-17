package com.broker.kafqa.repository;

import com.broker.kafqa.model.Topic;

import java.util.List;

public interface TopicRepository {
    /** Create a new topic; no-op if already exists */
    void createTopic(String name);

    /** Delete a topic and all its messages & subscriptions */
    void deleteTopic(String name);

    /** Find a topic by name, or null if not found */
    Topic findByName(String name);

    /** List all existing topics */
    List<Topic> listTopics();
}
