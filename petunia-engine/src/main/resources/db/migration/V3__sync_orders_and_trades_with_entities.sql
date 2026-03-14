-- Sync orders table with Order JPA entity
ALTER TABLE orders RENAME COLUMN player_name TO player_id;
-- Change type to UUID. Since we don't care about backward compatibility, 
-- we can just drop and recreation or use USING if there's data (but we are told not to be backward compatible)
-- However, to be safe and efficient:
ALTER TABLE orders ALTER COLUMN player_id TYPE UUID USING (player_id::UUID);

-- Fix enum issue for PostgreSQL
ALTER TABLE orders ALTER COLUMN side TYPE VARCHAR(10);
ALTER TABLE orders ALTER COLUMN type TYPE VARCHAR(10);

-- Sync trades table with Trade JPA entity
ALTER TABLE trades RENAME COLUMN buyer_name TO buyer_id;
ALTER TABLE trades ALTER COLUMN buyer_id TYPE UUID USING (buyer_id::UUID);

ALTER TABLE trades RENAME COLUMN seller_name TO seller_id;
ALTER TABLE trades ALTER COLUMN seller_id TYPE UUID USING (seller_id::UUID);
