# Clearingstelle

Verwaltet die Konten und Portfolios der Spieler in einer PostgreSQL Datenbank.

## Service Details
- **Port:** 8082
- **Technologie:** Spring Boot, Kotlin, Flyway, PostgreSQL

## Datenbank Schema

### player_account
Speichert die Kontoinformationen und das Guthaben der Spieler.
- `id`: UUID (Primary Key)
- `player_name`: String
- `balance`: Decimal (Spielgeld-Guthaben)
- `created_at`: Timestamp
- `updated_at`: Timestamp

### asset
Speichert die Wertpapierbestände (Portfolios) der Spieler.
- `id`: UUID (Primary Key)
- `account_id`: UUID (Foreign Key auf player_account)
- `symbol`: String (Kürzel der Mannschaft, z.B. "FCB")
- `quantity`: Long (Anzahl der Anteile)
- `created_at`: Timestamp
- `updated_at`: Timestamp



## REST API

### Player Accounts
- `GET /api/accounts`: List all player accounts.
- `GET /api/accounts/{id}`: Get account by ID.
- `POST /api/accounts`: Create a new player account.
  - Body: `{ "playerName": "string", "initialBalance": 100.00 }`
- `DELETE /api/accounts/{id}`: Delete a player account.

### Assets (Portfolio)
- `GET /api/accounts/{accountId}/assets`: List all assets for a specific account.
- `POST /api/accounts/{accountId}/assets`: Add an asset (or increase quantity) to an account.
  - Body: `{ "symbol": "string", "quantity": 10 }`
- `DELETE /api/accounts/{accountId}/assets/{symbol}?quantity={quantity}`: Remove a specific quantity of an asset from an account.

## Implementation Details

### Business Logic (ClearingService)
- **Account Management**: Supports creation with initial balance and deletion.
- **Asset Management**: 
    - `addAsset`: Automatically merges quantities if the player already owns the same asset (symbol).
    - `removeAsset`: Validates that the player has sufficient quantity before removal.

### Data Transfer Objects (DTOs)
- `PlayerAccountDto`: `id`, `playerName`, `balance`
- `CreatePlayerAccountRequest`: `playerName`, `initialBalance`
- `AssetDto`: `id`, `accountId`, `symbol`, `quantity`
- `CreateAssetRequest`: `symbol`, `quantity`
