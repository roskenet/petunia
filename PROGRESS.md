# ProjectPetunia Progress Report

## Summary
In the initial session, we established the core microservices architecture for ProjectPetunia, a stock market simulation game. We created the parent Maven project and three Spring Boot microservice stubs using Kotlin and Java 21.

## Completed Tasks

### 1. Root Project Initialization
- Created a root `pom.xml` to manage the multi-module project.
- Configured project-wide properties:
    - **Java Version**: 21
    - **Kotlin Version**: 1.9.25
    - **Spring Boot Version**: 3.2.3
- Added shared dependencies and plugins for Kotlin and Spring Boot.

### 2. Microservice Stubs Creation
Each microservice was initialized with its respective directory structure, `pom.xml`, and main application class.

#### Exchange Microservice
- **Directory**: `/exchange`
- **Port**: 8081
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `com.projectpetunia.exchange.ExchangeApplication.kt`

#### Bank Microservice
- **Directory**: `/bank`
- **Port**: 8082
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `com.projectpetunia.bank.BankApplication.kt`

#### Central Bank Microservice
- **Directory**: `/central-bank`
- **Port**: 8083
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver.
- **Entry Point**: `com.projectpetunia.centralbank.CentralBankApplication.kt`

### 3. Build Verification
- Successfully ran `mvn compile -DskipTests` to ensure all modules and the parent project build correctly with JDK 21.

## Project Structure Overview
```text
.
├── bank/
│   ├── pom.xml
│   └── src/main/kotlin/com/projectpetunia/bank/BankApplication.kt
├── central-bank/
│   ├── pom.xml
│   └── src/main/kotlin/com/projectpetunia/centralbank/CentralBankApplication.kt
├── exchange/
│   ├── pom.xml
│   └── src/main/kotlin/com/projectpetunia/exchange/ExchangeApplication.kt
├── pom.xml
└── README.md
```

## Next Steps
- Implement domain models and JPA entities for each service.
- Set up PostgreSQL database configurations.
- Implement REST controllers for basic functionalities.
- Configure inter-service communication (REST/Nakadi).
