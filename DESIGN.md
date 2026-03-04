# ProjectPetunia - Online Stock Market Simulation

> 🤖 **AI Tool Note**: This `DESIGN.md` is a context-rich specification optimized for AI agents. Humans should refer to [README.md](README.md) for a general overview.

**ProjectPetunia** is an online stock market simulation game where players trade virtual shares of teams from the German 1st Football Bundesliga. Market performance is driven by real-world sporting results.

---

## 🏗️ System Architecture

The project has transitioned from a microservices approach to a consolidated monolithic core for the main game logic, while maintaining a separate service for central bank functions.

### Core Modules

| Module | Responsibility |
| :--- | :--- |
| **Petunia Commons** (`/petunia-commons`) | Shared DTOs and enums between microservices. |
| **Petunia Engine** (`/engine`) | The core monolith. Combines **Exchange** (order book, trade execution) and **Bank** (player accounts, portfolios, clearing). |
| **Central Bank** (`/central-bank`) | Regulates the market, manages interest rates, and performs market interventions. |
| **Villadiana BFF** (`/villadiana`) | Backend-for-Frontend, handles sessions, authentication, and WebSocket communication. |
| **Alpicola Frontend** (`/alpicola`) | Next.js frontend for players to interact with the game. |

The **polonium** project is a legacy landing page for the domain. It is currently a standalone project, not integrated into the main Maven build or the Kubernetes deployment structure.

### Technical Stack

- **Language**: Kotlin 2.3.10 (Java 25)
- **Framework**: Spring Boot 4.0.3
- **Communication**: REST (Synchronous), OpenAPI/Swagger (SpringDoc 3.0.1)
- **Database**: PostgreSQL (managed via Flyway migrations)
- **Caching**: Valkey (Redis-compatible)
- **Testing**: JUnit 5, Cucumber (Gherkin)
- **Infrastructure**: Docker (Jib), Kubernetes (Local: Minikube; Prod: IONOS k3s)

---

## 🚀 Deployment

The project uses Kubernetes for deployment. 

Global infrastructure and module deployment manifests are in `deploy/k8s` for production deployment in IONOS k3s cluster, and `deploy/minikube` for local test deployment.

---

## 📜 Functional Requirements & Testing

All functional requirements are defined as executable specifications.
- **Methodology**: Behavior-Driven Development (BDD).
- **Format**: Gherkin `*.feature` files.
- **Location**: `<module>/src/test/resources/features/`
- **Verification**: All features MUST be tested via these Gherkin files to ensure business logic correctness.

---

## 🕹️ Game Mechanics

### 1. Players & Accounts
Players receive virtual starting capital. The **Petunia Engine** manages `PlayerAccount` and `Asset` (portfolio) entities.

### 2. Trading (Exchange)
The **Exchange** component manages the `Order` lifecycle.
- **Order Lifecycle**: Place -> Match (Planned) -> Execute/Settle (via Clearing).
- **Orders**: Buy/Sell, Market/Limit.
- **Matching**: Currently a placeholder in `OrderBookService`; full matching engine implementation is pending.

### 3. Settlement & Clearing
The **Bank** component handles the movement of assets and funds.
- **Clearing**: `ClearingService` provides `clearTrade()` to atomically update buyer/seller balances and asset quantities.
- **Dividends**: Calculated based on real-world Bundesliga match results.

---

## 🛠️ Development Context

### Project Structure
```text
.
├── petunia-commons/     # Shared DTOs and Enums
├── engine/              # Main Monolith (Exchange + Bank)
├── alpicola/            # Next.js Frontend
│   └── src/             # React components and pages
├── villadiana/          # Backend-for-Frontend (BFF)
├── central-bank/        # Market Regulation service
├── deploy/              # Global deployment configurations (e.g., Valkey)
├── pom.xml              # Parent Maven POM
└── PROGRESS.md          # Real-time status tracker
```

### Entry Points
- **Petunia Engine**: `de.roskenet.petunia.PetuniaEngine` (Port 8080)
- **Central Bank**: `de.roskenet.petunia.centralbank.CentralBankApplication` (Port 8083)

### Database Schema
- **Flyway**: Migrations are located in `src/main/resources/db/migration/` within each module.
- **Engine Schema**: Combines both bank and exchange tables.

---

## 🤖 AI Agent Guidelines

1. **Maintain Logical Boundaries**: Although merged into the `engine` monolith, strictly maintain package separation between `de.roskenet.petunia.bank` and `de.roskenet.petunia.exchange`.
2. **Service Interaction**: Since these packages may eventually split into microservices, minimize direct cross-package dependency. Communicate via clearly defined service interfaces.
3. **API-First**: Always maintain and extend the REST APIs in the `engine`. This ensures the monolith remains compatible with the `villadiana` BFF and future service clients.
4. **Context First**: Consult `PROGRESS.md` before making changes to understand the current implementation state.
5. **Test-Driven (BDD)**: When implementing business logic, start by defining Gherkin features in `<module>/src/test/resources/features/`.
6. **Code Style**: Use idiomatic Kotlin and Spring Boot best practices. Ensure all new JPA entities include appropriate Flyway migrations.

---
Made with ❤️ by [felix@roskenet.de](mailto:felix@roskenet.de)

