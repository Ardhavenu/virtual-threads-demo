# Java Concurrency Demo: Single Thread, Traditional Threads, CompletableFuture, and Virtual Threads

This project demonstrates four different ways of calling the same fake API multiple times in Java.
The goal is to understand the evolution from simple blocking code to traditional multithreading, async programming, and Java 21 virtual threads.

## Requirements

- **Java 21** or later
- **IntelliJ IDEA**
- **Maven** project

### Recommended pom.xml compiler settings:
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>

## Demo Scenario

We simulate one external API call:
callFakeApi(id)

Each API call takes 1 second:
Thread.sleep(1000);

Think of this as calling:
GET /customer/{id}

We call this fake API multiple times to compare different concurrency models.

