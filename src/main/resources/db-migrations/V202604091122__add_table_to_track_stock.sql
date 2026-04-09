CREATE TABLE geli.stock_movements (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                      item_id UUID NOT NULL REFERENCES geli.items(id),
                                      variant_id UUID REFERENCES geli.variants(id),
                                      movement_type VARCHAR(20) NOT NULL CHECK (movement_type IN ('STOCK_IN', 'STOCK_OUT', 'INITIAL_STOCK')),
                                      quantity_before INTEGER NOT NULL,
                                      quantity_change INTEGER NOT NULL,
                                      quantity_after INTEGER NOT NULL,
                                      notes TEXT,
                                      created_at TIMESTAMP,
                                      updated_at TIMESTAMP,
                                      deleted_at TIMESTAMP DEFAULT NULL
);

CREATE INDEX idx_stock_movements_item ON geli.stock_movements(item_id);
CREATE INDEX idx_stock_movements_variant ON geli.stock_movements(variant_id);
CREATE INDEX idx_stock_movements_type ON geli.stock_movements(movement_type);
CREATE INDEX idx_stock_movements_created ON geli.stock_movements(created_at DESC);