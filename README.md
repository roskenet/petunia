# 🚀 ProjectPetunia

![Project Status](https://img.shields.io/badge/status-Alpha-orange)  
![Tech Stack](https://img.shields.io/badge/stack-React%2FNext.js%20|%20SpringBoot%2FKotlin-blue)  
![Database](https://img.shields.io/badge/database-PostgreSQL-lightgrey)

**ProjectPetunia** is an online stock market simulation game, where football meets finance.

---

## 📖 Table of Contents

- [Game Concept](#game-concept)  
- [Game Elements](#game-elements)  
  - [Players](#players)  
  - [Shares](#shares)  
  - [Dividends](#dividends)  
- [Implementation](#implementation)  
  - [Petunia Engine](#petunia-engine)
  - [Central Bank](#central-bank)
  - [Alpicola Frontend](#alpicola-frontend)
- [Technical Implementation](#technical-implementation)  
  - [Deployment](#deployment)  

---

## 🎯 Game Concept

The game simulates trading of securities on the fictional stock exchange **Petunia**.

The securities represent shares in teams from Germany’s **1st Football Bundesliga**.  
The economic performance of these “companies” is driven by their **real-world sporting success or failure**.

> ⚡ Fun fact: This links real-world events with an online game, while subtly introducing a sports betting element.

---

## 🕹️ Game Elements

### Players

Players are the market participants of the exchange.  

- Can join anytime  
- Receive a set amount of virtual starting capital  
- Have an online account and portfolio  
- Can view balances, holdings, and place buy/sell orders  

### Shares

Virtual shares represent Bundesliga teams.  

- Each team has a fixed number of shares at game start  
- Players can freely trade shares  

### Dividends

Dividends are paid after **real Bundesliga match days**.  

- Based on team performance  
- Formula factors include:
  - Opponent’s league position  
  - Goal difference  
  - Home vs away match  

> 💡 Note: Formula is still to be defined, keeping room for tuning game balance.

---

## 🏛️ Implementation

### Petunia Engine

The Petunia Engine is the core of the game. It combines the functionality of the **Exchange** and the **Bank**:

- **Exchange**: Acts as the trading venue, receives buy/sell orders, executes trades, and determines share prices.
- **Bank**: Handles player accounts and portfolios, acts as a clearing house, and calculates dividends.

Players interact with the game through the engine API.

### Central Bank

The central bank is the **game engine**:

- Steers the game indirectly  
- Can regulate market via interest rates or other interventions (e.g., inflation control)  
- May act as a trader to maintain liquidity  

> ⚠️ Its powers extend far beyond a real-world central bank — it’s the puppet master of Petunia.

### Alpicola Frontend

The **Alpicola** is the frontend application where players can:

- View their account balances and portfolios.
- Place buy and sell orders.
- See current market data and share prices.

It is built with React and Next.js.

---

## 💻 Technical Implementation

> 🛠️ **Environment Setup**: Before interacting with the code, switch to the correct Java SDK:
> ```bash
> sdk use java 25.0.2-tem
> ```
> All Maven commands must use the wrapper: `./mvnw`

- **Frontend**: React / Next.js, TypeScript  
- **Microservices**: Spring Boot / Kotlin, REST interfaces  
- **Database**: PostgreSQL (additional databases or Nakadi server optional)  
- **Auth & Users**: Keycloak  

### Deployment

- Local testing → `minikube`  
- Production → `k3s`  

> 📁 Each component has its own detailed spec in respective subfolders.

---

Made with ❤️  by felix@roskenet.de 

