# ProjectPetunia Overview

**ProjectPetunia** is an online stock market simulation game.

## Game Concept

The game simulates the trading of securities on the fictional stock exchange “Petunia”.

The securities represent shares in teams from Germany’s 1st Football Bundesliga.  
The economic performance of the companies in the game is driven by their real-world sporting success or failure.

This creates a link between real-world events and the online game, while also introducing — indirectly — an element similar to sports betting.

## Game Elements

The game consists of the following components:

### Players

Players are the market participants on the exchange.

They can join the game at any time and receive a certain amount of virtual starting capital.

Just like in the real world, they are given:

- an online account  
- an online securities portfolio  

There they can view their balance and holdings, and place buy and sell orders for shares.

### Shares

On the fictional exchange, virtual shares of Bundesliga teams can be traded.

At the start of the game, each team has a fixed number of shares available for trading.

### Dividends

Dividends are paid out after real Bundesliga match days.

The dividend amount depends on the team’s sporting performance on that match day.

A formula (to be defined) will take into account factors such as:

- the opponent’s league position  
- goal difference  
- whether the match was played at home or away  

## Implementation

The following institutions are required for implementation:

### Exchange

The exchange manages the order book and acts as the trading venue.

It:

- receives buy and sell orders from players  
- executes trades  
- acts as the price broker  
- determines and stores share prices  

### Bank

The bank manages player accounts and portfolios.

It acts as the clearing entity responsible for transferring:

- securities  
- funds  

between players and portfolios.

Players interact with the game exclusively through the bank.

After each real match day, the bank receives the football results and calculates the dividends to be paid to shareholders.

### Central Bank

The central bank serves as the game’s core engine.

Its responsibilities go far beyond those of a real-world central bank.

It indirectly steers the game and can regulate the market through:

- interest rates  
- other interventions  

for example to combat inflation.

It may also act as a trader itself in order to maintain market liquidity.

## Technical Implementation

Frontends will be built using:

- React / Next.js  
- TypeScript  

All microservices are Spring Boot / Kotlin web applications, typically communicating via REST interfaces.

Data is generally stored in a PostgreSQL database.

A Nakadi server or additional databases may be used for specific purposes.

User management and authentication will be handled via a Keycloak server.

### Deployment

- Local testing → minikube  
- Live operation → k3s  

Each technical component has its own detailed specification in its respective subdirectory.

