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

### Technical Stack

- **Language**: Kotlin 2.3.10 (Java 25)
- **Framework**: Spring Boot 4.0.3
- **Communication**: REST (Synchronous), OpenAPI/Swagger
- **Database**: PostgreSQL (managed via Flyway migrations)
- **Testing**: JUnit 5, Cucumber (Gherkin)
- **Infrastructure**: Docker (Jib), Minikube/K3s, Valkey (Redis)

---

## 🚀 Deployment

The project uses Kubernetes for deployment. Configuration files are located in the `deploy/` directory and within individual modules.

### Global Infrastructure
- **Valkey**: Located in `deploy/k8s/valkey/`. Provides a Redis-compatible data store used by services like `villadiana`.

### Module Deployment
Each module contains its own `deploy/` or `Makefile` for deployment:
- **Alpicola**: `alpicola/deploy/` (Kubernetes manifests) and `alpicola/Makefile`.
- **Villadiana**: `villadiana/deploy/` (Kubernetes manifests and environment configurations).

---

## 📜 Functional Requirements & Testing

All functional requirements are defined as executable specifications.
- **Methodology**: Behavior-Driven Development (BDD).
- **Format**: Gherkin `*.feature` files.
- **Location**: `engine/src/test/resources/features/`
- **Verification**: All features MUST be tested via these Gherkin files to ensure business logic correctness.

---

## 🕹️ Game Mechanics

### 1. Players & Accounts
Players receive virtual starting capital. The **Petunia Engine** manages `PlayerAccount` and `Asset` (portfolio) entities.

### 2. Trading (Exchange)
The Exchange component within the Engine manages the `Order` lifecycle (Place -> Match -> Execute). 
- **Orders**: Buy/Sell, Market/Limit (to be expanded).
- **Matching**: Handled by `OrderBookService`.

### 3. Dividends
Calculated based on real-world Bundesliga match results. Factors include opponent strength, goal difference, and venue.

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

1. **Monolith Boundary**: Although merged into `engine`, maintain logical separation between `de.roskenet.petunia.bank` and `de.roskenet.petunia.exchange` packages.
2. **Context First**: Always check `PROGRESS.md` for the latest implementation status before suggesting changes.
3. **Test-Driven**: When implementing new features, start by defining the Gherkin feature in a `.feature` file.
4. **Style**: Follow Kotlin idiomatic patterns and Spring Boot best practices.

---
Made with ❤️ by [felix@roskenet.de](mailto:felix@roskenet.de)

