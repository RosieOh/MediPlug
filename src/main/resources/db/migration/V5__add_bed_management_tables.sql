-- Bed Management Tables
CREATE TABLE bed (
    bed_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    bed_number VARCHAR(20) NOT NULL,
    bed_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    daily_rate DECIMAL(10,2) NOT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (room_id) REFERENCES room(room_id)
);

CREATE TABLE admission (
    admission_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    bed_id BIGINT NOT NULL,
    admission_number VARCHAR(50) NOT NULL,
    admission_date TIMESTAMP NOT NULL,
    discharge_date TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'ADMITTED',
    admission_reason VARCHAR(100) NOT NULL,
    discharge_summary VARCHAR(500),
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (bed_id) REFERENCES bed(bed_id)
);

-- Indexes for Bed Management tables
CREATE INDEX idx_bed_room_id ON bed(room_id);
CREATE INDEX idx_bed_bed_number ON bed(bed_number);
CREATE INDEX idx_bed_status ON bed(status);
CREATE INDEX idx_bed_bed_type ON bed(bed_type);

CREATE INDEX idx_admission_patient_id ON admission(patient_id);
CREATE INDEX idx_admission_bed_id ON admission(bed_id);
CREATE INDEX idx_admission_admission_number ON admission(admission_number);
CREATE INDEX idx_admission_status ON admission(status);
CREATE INDEX idx_admission_admission_date ON admission(admission_date);
CREATE INDEX idx_admission_discharge_date ON admission(discharge_date);
