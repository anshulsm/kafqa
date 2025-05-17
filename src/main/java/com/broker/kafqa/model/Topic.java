package com.broker.kafqa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic {
    private final String name;
    private final Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();

    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    /**
     * Enqueue a message into this topic.
     * @param message message
     */
    public void enqueue(Message message) {
        messageQueue.add(message);
    }

    /**
     * Get the head of the queue without removing.
     */
    public Message peek() {
        return messageQueue.peek();
    }

    /**
     * Poll the next message from the queue.
     */
    public Message poll() {
        return messageQueue.poll();
    }

    /**
     * Register a new subscriber to this topic.
     */
    public void addSubscription(Subscription sub) {
        subscriptions.add(sub);
    }

    /**
     * Remove an existing subscription by its ID.
     */
    public void removeSubscription(UUID subscriptionId) {
        subscriptions.removeIf(s -> s.getId().equals(subscriptionId));
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messageQueue);
    }
}
