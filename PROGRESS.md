# ProjectPetunia Progress Report

## Summary
In the initial session, we established the core microservices architecture for ProjectPetunia, a stock market simulation game. We created the parent Maven project and three Spring Boot microservice stubs using Kotlin and Java 21.

## Completed Tasks

### 1. Root Project Initialization
- Created a root `pom.xml` to manage the multi-module project.
- Configured project-wide properties:
    - **Java Version**: 25
    - **Kotlin Version**: 2.3.10
    - **Spring Boot Version**: 4.0.3
- Added shared dependencies and plugins for Kotlin and Spring Boot.

### 2. Microservice Stubs Creation
Each microservice was initialized with its respective directory structure, `pom.xml`, and main application class.

#### Exchange Microservice
- **Directory**: `/exchange`
- **Port**: 8081
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `de.roskenet.petunia.exchange.ExchangeApplication.kt`

#### Bank Microservice
- **Directory**: `/bank`
- **Port**: 8082
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `de.roskenet.petunia.bank.BankApplication.kt`

#### Central Bank Microservice
- **Directory**: `/central-bank`
- **Port**: 8083
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `de.roskenet.petunia.centralbank.CentralBankApplication.kt`

### 3. Build Verification
- Successfully ran `mvn compile -DskipTests` to ensure all modules and the parent project build correctly with JDK 25.
- Integrated `jib-maven-plugin` version 3.5.1 for Docker image creation.
- Verified Docker image creation for all services (`bank`, `exchange`, `central-bank`) using `mvn jib:dockerBuild`.
- Added `springdoc-openapi-starter-webmvc-ui` version 3.0.1 to all microservices for Swagger UI support.

### 4. Exchange Order Book Implementation
- Implemented `Order` entity, `OrderSide`, and `OrderStatus` enums.
- Created `OrderRepository` for database access.
- Implemented `OrderBookService` to handle order placement.
- Added `OrderController` with REST endpoints for placing and listing orders.
- Configured Flyway for database migrations and added initial schema.

## Project Structure Overview
```text
.
├── bank/
│   ├── pom.xml
│   └── src/main/kotlin/de/roskenet/petunia/bank/BankApplication.kt
├── central-bank/
│   ├── pom.xml
│   └── src/main/kotlin/de/roskenet/petunia/centralbank/CentralBankApplication.kt
├── exchange/
│   ├── pom.xml
│   └── src/main/kotlin/de/roskenet/petunia/exchange/ExchangeApplication.kt
├── pom.xml
└── README.md
```

## Next Steps
- Implement matching engine in Exchange service.
- Implement inter-service communication between Exchange and Bank for trade settlement.
- Implement domain models and JPA entities for Central Bank service.
- Configure CI/CD pipeline for automatic Docker image builds.
