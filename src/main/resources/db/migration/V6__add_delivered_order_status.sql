ALTER TABLE orders
    DROP CONSTRAINT orders_status_check;

ALTER TABLE orders
    ADD CONSTRAINT chk_orders_status
    CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'DELIVERED'));