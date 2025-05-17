# Kafqa Pub/Sub Broker

A simple in-JVM, in-memory Publish–Subscribe broker built with Spring Boot.  
It lets you create topics, publish messages, and subscribe in PUSH or PULL modes—all over REST—so you can learn how real brokers like Kafka or RabbitMQ work under the hood.

---

## 📖 Overview

### What is the Pub/Sub Model?
- **Publishers** send messages (events) to *topics* without knowing who will consume them.
- **Subscribers** register interest in one or more topics and receive messages asynchronously.
- A **Broker** sits in the middle, decoupling producers from consumers and handling routing, delivery guarantees, retries, and (optionally) dead-lettering.

### How It Works in Kafqa
1. **Topic Management**:
    - Create/Delete topics via `POST /topics` and `DELETE /topics/{name}`.
    - The broker keeps an in-memory queue per topic.

2. **Publish**:
    - `POST /topics/{name}/publish` — wrap your payload in a `Message`, enqueue it, and fan-out to all subscribers.

3. **Subscribe**:
    - `POST /topics/{name}/subscriptions` with `"mode": "PUSH"` or `"PULL"`.
    - Returns a `subscriptionId` you’ll use for consuming or acknowledging.

4. **Consume & Ack (PULL mode)**:
    - `GET  /topics/{name}/subscriptions/{subscriptionId}/consume`
    - `POST /topics/{name}/subscriptions/{subscriptionId}/ack`

5. **Push Delivery**:
    - For PUSH subscribers, the broker will `POST` each `Message` to your callback URL.

---

## 🔧 Technology Stack

- **Java 17** & **Spring Boot 3.x**
- **Maven** for build & dependency management
- **SLF4J/Logback** for logging
- **Spring Boot Actuator** for health & metrics

---

## ⚙️ Concurrency & Thread Safety

We use `ConcurrentHashMap<String, Topic>` in our `InMemoryTopicRepository` so that multiple threads can safely:

- Create, fetch, and delete topics
- Enqueue messages and fan-out to subscribers at the same time

Inside each `Topic`, a `ConcurrentLinkedQueue<Message>` holds the ordered events, and a `CopyOnWriteArrayList<Subscription>` tracks active subscribers without extra locking.

This mimics the high-throughput, thread-safe design of real brokers.

---

## 🚀 Running Locally

### Prerequisites
- JDK 17+ installed
- Maven 3.6+

### Steps

1. **Clone & build**
   ```bash
   git clone https://github.com/Anshul140/message-broker.git
   cd kafqa-broker
   mvn clean package
