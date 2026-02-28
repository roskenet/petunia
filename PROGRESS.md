# ProjectPetunia Progress Report

## Summary
The ProjectPetunia core architecture has been transitioned to a monolithic structure for the main game logic. The `exchange` and `bank` submodules were merged into a single module, now referred to as the **Petunia Engine**.

## Completed Tasks

### 1. Root Project Initialization
- Created a root `pom.xml` to manage the project modules.
- Configured project-wide properties:
    - **Java Version**: 25
    - **Kotlin Version**: 2.3.10
    - **Spring Boot Version**: 4.0.3
- Added shared dependencies and plugins for Kotlin and Spring Boot.

### 2. Monolith Transition
- Merged `exchange` submodule into `bank`.
- Combined `exchange` and `bank` source code and resources.
- Renamed the combined module to `engine` to serve as the main monolith.

#### Petunia Engine (formerly Bank/Exchange)
- **Directory**: `/engine`
- **Port**: 8080
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver, Flyway, Cucumber (for testing).
- **Entry Point**: `de.roskenet.petunia.PetuniaEngine`
- **Package structure**: `de.roskenet.petunia` (Main class moved from `de.roskenet.petunia.bank`)

#### Central Bank Microservice
- **Directory**: `/central-bank`
- **Port**: 8083
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `de.roskenet.petunia.centralbank.CentralBankApplication`

### 3. Build Verification
- Successfully ran `./mvnw compile -DskipTests` to ensure the engine and the parent project build correctly with JDK 25.
- Integrated `jib-maven-plugin` version 3.5.1 for Docker image creation.
- Verified Docker image creation using `./mvnw jib:dockerBuild`.

### 4. Exchange Order Book Implementation (now in Engine)
- Implemented `Order` entity, `OrderSide`, and `OrderStatus` enums.
- Created `OrderRepository` for database access.
- Implemented `OrderBookService` to handle order placement.
- Added `OrderController` with REST endpoints for placing and listing orders.
- Configured Flyway for database migrations and consolidated schemas.

## Project Structure Overview
```text
.
├── engine/
│   ├── pom.xml
│   └── src/main/kotlin/de/roskenet/petunia/
│       ├── PetuniaEngine.kt
│       ├── bank/
│       └── exchange/
├── central-bank/
│   ├── pom.xml
│   └── src/main/kotlin/de/roskenet/petunia/centralbank/CentralBankApplication.kt
├── pom.xml
└── README.md
```

## Next Steps
- Implement matching engine within the Petunia Engine.
- Refactor internal communication between exchange and bank components to use direct service calls.
- Implement domain models and JPA entities for Central Bank service.
