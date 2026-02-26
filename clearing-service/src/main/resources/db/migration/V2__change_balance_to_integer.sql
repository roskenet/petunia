-- Change balance from DECIMAL to BIGINT (storing cents instead of euros)
ALTER TABLE player_account ALTER COLUMN balance TYPE BIGINT USING (balance * 100)::BIGINT;
ALTER TABLE player_account ALTER COLUMN balance SET DEFAULT 0;

