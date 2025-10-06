-- Pharmacy Tables
CREATE TABLE drug_master (
    drug_master_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drug_code VARCHAR(50) NOT NULL UNIQUE,
    drug_name VARCHAR(200) NOT NULL,
    generic_name VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    dosage_form VARCHAR(50) NOT NULL,
    strength VARCHAR(50) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    shelf_life INT NOT NULL,
    prescription_required BOOLEAN NOT NULL DEFAULT TRUE,
    controlled BOOLEAN NOT NULL DEFAULT FALSE,
    side_effects TEXT,
    contraindications TEXT,
    storage_conditions TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system'
);

CREATE TABLE drug_inventory (
    drug_inventory_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drug_master_id BIGINT NOT NULL,
    lot_number VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    available_quantity INT NOT NULL,
    expiration_date DATE NOT NULL,
    unit_cost DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (drug_master_id) REFERENCES drug_master(drug_master_id)
);

-- Indexes for Pharmacy tables
CREATE INDEX idx_drug_master_drug_code ON drug_master(drug_code);
CREATE INDEX idx_drug_master_drug_name ON drug_master(drug_name);
CREATE INDEX idx_drug_master_category ON drug_master(category);
CREATE INDEX idx_drug_master_active ON drug_master(active);

CREATE INDEX idx_drug_inventory_drug_master_id ON drug_inventory(drug_master_id);
CREATE INDEX idx_drug_inventory_lot_number ON drug_inventory(lot_number);
CREATE INDEX idx_drug_inventory_expiration_date ON drug_inventory(expiration_date);
CREATE INDEX idx_drug_inventory_status ON drug_inventory(status);
