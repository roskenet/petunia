# ProjectPetunia - Online Stock Market Simulation

> 🤖 **AI Tool Note**: This `DESIGN.md` is primarily meant to be read by AI tools. Humans should refer to [README.md](README.md).

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

- **Language**: Kotlin 2.3.10 (targeting Java 25)
- **Framework**: Spring Boot 4.0.3
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
- **Java SDK**: Switch to the correct SDK version:
  ```bash
  sdk use java 25.0.2-tem
  ```
- **Maven**: All interactions with the code must use the wrapper: `./mvnw`
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
./mvnw clean install
```

To run a specific service:
```bash
./mvnw spring-boot:run -pl <module-name>
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

