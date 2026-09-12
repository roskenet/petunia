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

### 3. Build Verification
- Successfully ran `./mvnw compile -DskipTests` to ensure the engine and the parent project build correctly with JDK 25.
- Integrated `jib-maven-plugin` version 3.5.1 for Docker image creation.
- Verified Docker image creation using `./mvnw jib:dockerBuild`.

### 4. Frontend Integration
- Copied `alpicola` (Next.js/TypeScript frontend) into the root project.
- Updated root `.gitignore` with Next.js and Node.js entries.
- Updated project documentation (`README.md`, `DESIGN.md`) to include the frontend.

### 5. Backend-for-Frontend (BFF) Integration
- Added `villadiana` (Spring Boot/Java BFF) to the project.
- Updated documentation to include `villadiana`'s role in the architecture.
- Added `villadiana` as a module in the root `pom.xml`.

### 6. Deployment Infrastructure
- Added root `deploy/` directory with Kubernetes manifests.
- Implemented **Valkey** (Redis) deployment in `deploy/k8s/valkey/`.
- Integrated component-specific deployment scripts in `alpicola/deploy/` and `villadiana/deploy/`.
- Added `Makefile` in `alpicola/` for simplified deployment tasks.

### 7. Exchange Order Book Implementation (now in Engine)
- Implemented `Order` entity, `OrderSide`, and `OrderType` enums.
- Removed `OrderStatus` and refactored the system to delete completed or cancelled orders from the order book, maintaining only open orders in the `orders` table.
- Created `OrderRepository` for database access.
- Implemented `OrderBookService` with a matching engine supporting price-time priority for Limit and Market orders.
- Market orders follow "Fill or Kill" logic, where any unfilled portion is immediately removed.
- Added `OrderController` with REST endpoints for placing and listing orders.
- Configured Flyway for database migrations and consolidated schemas.

## Project Structure Overview
```text
.
├── engine/
├── alpicola/
│   └── src/
├── villadiana/
├── deploy/
├── pom.xml
└── README.md
```

## Next Steps
- Refactor internal communication between exchange and bank components to use direct service calls.
