CREATE TABLE geli.categories (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 code VARCHAR(50) UNIQUE NOT NULL,
                                 name VARCHAR(255) NOT NULL,
                                 description TEXT,
                                 parent_id UUID REFERENCES geli.categories(id),
                                 created_at TIMESTAMP,
                                 updated_at TIMESTAMP,
                                 deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE geli.brands (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             code VARCHAR(50) UNIQUE NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             description TEXT,
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP,
                             deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE geli.items (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            product_code VARCHAR(50) UNIQUE NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            description TEXT,
                            category_id UUID REFERENCES geli.categories(id),
                            brand_id UUID REFERENCES geli.brands(id),
                            price DECIMAL(15, 2) NOT NULL CHECK (price >= 0),
                            stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
                            warehouse_zone VARCHAR(10),
                            rack_number VARCHAR(10),
                            created_at TIMESTAMP,
                            updated_at TIMESTAMP,
                            deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE geli.variants (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               item_id UUID NOT NULL REFERENCES geli.items(id) ON DELETE CASCADE,
                               variant_name VARCHAR(100) NOT NULL,
                               variant_value VARCHAR(100) NOT NULL,
                               price DECIMAL(15, 2) CHECK (price >= 0),
                               stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
                               warehouse_zone VARCHAR(10),
                               rack_number VARCHAR(10),
                               created_at TIMESTAMP,
                               updated_at TIMESTAMP,
                               deleted_at TIMESTAMP DEFAULT NULL,
                               UNIQUE(item_id, variant_name, variant_value)
);

CREATE INDEX idx_items_product_code ON geli.items(product_code) WHERE deleted_at IS NULL;
CREATE INDEX idx_items_category ON geli.items(category_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_items_brand ON geli.items(brand_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_items_name ON geli.items(name) WHERE deleted_at IS NULL;
CREATE INDEX idx_variants_item ON geli.variants(item_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_categories_code ON geli.categories(code) WHERE deleted_at IS NULL;
CREATE INDEX idx_brands_code ON geli.brands(code) WHERE deleted_at IS NULL;

-- master data
INSERT INTO geli.categories (code, name, description) VALUES
('WET_FOOD', 'Makanan Basah', 'Makanan kucing basah'),
('DRY_FOOD', 'Makanan Kering', 'Makanan kucing kering'),
('TREAT', 'Snack', 'Snack kucing');

INSERT INTO geli.brands (code, name, description) VALUES
('WHK', 'Whiskas', 'Makanan kucing Whiskas'),
('RC', 'Royal Canin', 'Royal Canin'),
('MEO', 'Me-O', 'Me-O snack');