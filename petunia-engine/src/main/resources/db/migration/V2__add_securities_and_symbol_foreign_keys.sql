CREATE TABLE securities (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO securities (symbol, name)
SELECT symbol, symbol
FROM (
    SELECT DISTINCT symbol FROM asset
    UNION
    SELECT DISTINCT symbol FROM orders
    UNION
    SELECT DISTINCT symbol FROM trades
) AS distinct_symbols
ON CONFLICT (symbol) DO NOTHING;

ALTER TABLE asset
    ADD CONSTRAINT fk_asset_symbol_security
    FOREIGN KEY (symbol) REFERENCES securities(symbol);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_symbol_security
    FOREIGN KEY (symbol) REFERENCES securities(symbol);

ALTER TABLE trades
    ADD CONSTRAINT fk_trades_symbol_security
    FOREIGN KEY (symbol) REFERENCES securities(symbol);

