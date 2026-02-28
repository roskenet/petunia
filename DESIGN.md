# ProjectPetunia - Online Stock Market Simulation

**ProjectPetunia** is an online stock market simulation game where players trade virtual shares of teams from the German 1st Football Bundesliga. The economic performance of the "companies" (teams) is driven by their real-world sporting results.

---

## 🧩 System Architecture

ProjectPetunia is built as a microservices-based distributed system.

### Core Components

| Component | Responsibility |
| :--- | :--- |
| **Exchange** | Manages the order book, executes trades, and serves as the price broker. |
| **Bank** | Manages player accounts and portfolios; acts as the clearing house for funds and securities. |
| **Central Bank** | The game's engine; regulates the market via interest rates and interventions. |

### Technical Stack

- **Language**: Kotlin 1.9.25 (targeting Java 21)
- **Framework**: Spring Boot 3.2.3
- **Communication**: REST (Synchronous), potential Nakadi (Asynchronous)
- **Database**: PostgreSQL
- **Security**: Keycloak (Authentication & User Management)
- **Frontend**: React / Next.js with TypeScript
- **Infrastructure**: Minikube (Local), K3s (Production)

---

## 🕹️ Game Mechanics

### 1. Players
Participants receive virtual starting capital. They interact with the game primarily through the **Bank** to manage their account and portfolio.

### 2. Shares
Virtual shares representing real Bundesliga teams. Each team has a fixed supply of shares available at the start.

### 3. Dividends
Paid out after each real-world match day. The amount is calculated by the **Bank** based on a formula considering:
- Opponent's league position
- Goal difference
- Match venue (Home/Away)

---

## 🛠️ Development Guide

### Prerequisites
- JDK 21
- Maven 3.9+
- Docker & PostgreSQL

### Project Structure
```text
.
├── bank/             # Bank microservice
├── central-bank/     # Central Bank microservice
├── exchange/         # Exchange microservice
├── pom.xml           # Root Maven descriptor
├── PROGRESS.md       # Current project status and roadmap
└── README.md         # This file
```

### Build and Run
To build the entire project:
```bash
mvn clean install
```

To run a specific service:
```bash
mvn spring-boot:run -pl <module-name>
```

---

## 🤖 AI Agent Context

When working on this project, please adhere to the following:
- **Module Boundary**: Ensure strict separation of concerns between Exchange, Bank, and Central Bank.
- **Entry Points**: Each service has a standard Spring Boot entry point in `src/main/kotlin/com/projectpetunia/<module>/<Module>Application.kt`.
- **Database**: Each service has its own database schema (configured in `application.yml`).
- **Dependencies**: Shared configurations are managed in the root `pom.xml`.
- **Specs**: Refer to subdirectories for specific logic or specs related to each component (once implemented).

---

Made with ❤️ by [felix@roskenet.de](mailto:felix@roskenet.de)

