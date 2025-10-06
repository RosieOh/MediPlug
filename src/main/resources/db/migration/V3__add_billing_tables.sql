-- Billing Tables
CREATE TABLE procedure_code (
    procedure_code_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system'
);

CREATE TABLE billing (
    billing_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    procedure_code_id BIGINT NOT NULL,
    billing_number VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    insurance_amount DECIMAL(10,2) NOT NULL,
    copay_amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(50) NOT NULL,
    paid_at TIMESTAMP,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (procedure_code_id) REFERENCES procedure_code(procedure_code_id)
);

CREATE TABLE receipt (
    receipt_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    total_amount DECIMAL(10,2) NOT NULL,
    insurance_amount DECIMAL(10,2) NOT NULL,
    copay_amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ISSUED',
    issued_at TIMESTAMP NOT NULL,
    cancelled_at TIMESTAMP,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
);

-- Indexes for Billing tables
CREATE INDEX idx_procedure_code_code ON procedure_code(code);
CREATE INDEX idx_procedure_code_category ON procedure_code(category);
CREATE INDEX idx_procedure_code_active ON procedure_code(active);

CREATE INDEX idx_billing_patient_id ON billing(patient_id);
CREATE INDEX idx_billing_appointment_id ON billing(appointment_id);
CREATE INDEX idx_billing_procedure_code_id ON billing(procedure_code_id);
CREATE INDEX idx_billing_billing_number ON billing(billing_number);
CREATE INDEX idx_billing_status ON billing(status);
CREATE INDEX idx_billing_paid_at ON billing(paid_at);

CREATE INDEX idx_receipt_patient_id ON receipt(patient_id);
CREATE INDEX idx_receipt_receipt_number ON receipt(receipt_number);
CREATE INDEX idx_receipt_status ON receipt(status);
CREATE INDEX idx_receipt_issued_at ON receipt(issued_at);
