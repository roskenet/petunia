# Initialization Steps

This document records the initial setup of the Petunia Stock Market Simulation project.

## Project Structure

The project is initialized as a Maven multi-module project with a parent aggregator POM and several Spring Boot/Kotlin submodules.

### Parent POM (`petunia-parent`)
- **Location:** Project root (`pom.xml`)
- **Type:** Aggregator POM
- **Spring Boot Version:** 3.2.3
- **Java Version:** 21
- **Kotlin Version:** 1.9.22
- **Modules:**
  - `petunia-monolith`: The initial monolithic implementation.
  - `orderbook-service`: Microservice for order management.
  - `clearing-service`: Microservice for clearing and banking.
  - `boersenaufsicht-service`: Microservice for regulatory oversight.
  - `boerse-service`: Microservice for the exchange and game engine.

## Module Details

All modules are configured as Spring Boot applications using Kotlin.

### Common Configuration
- **Kotlin Compiler Plugins:** `spring` and `jpa` (all-open and no-arg).
- **Base Dependencies:** Web, Actuator, Kotlin Stdlib, Kotlin Reflect, Test.

### Service Ports
- `petunia-monolith`: 8080
- `orderbook-service`: 8081
- `clearing-service`: 8082
- `boersenaufsicht-service`: 8083
- `boerse-service`: 8084

### Persistence
- `orderbook-service` and `clearing-service` include Data JPA and PostgreSQL dependencies as per requirements.

## Initial Endpoints
Each service includes a basic REST controller (`HelloController`) providing a `GET /api/hello` endpoint for connectivity testing.

## Build Notes
- The project is configured for Java 21.
- Note: A build failure was observed on environments using JDK 25+ due to Kotlin 1.9.22 compatibility issues. It is recommended to use JDK 21 for builds.
