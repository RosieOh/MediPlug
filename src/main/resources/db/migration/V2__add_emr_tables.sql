-- EMR Tables
CREATE TABLE medical_record (
    medical_record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    chief_complaint TEXT NOT NULL,
    present_illness TEXT,
    past_history TEXT,
    physical_examination TEXT,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

CREATE TABLE chart_template (
    chart_template_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    template_content TEXT NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system'
);

CREATE TABLE prescription (
    prescription_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    drug_name VARCHAR(100) NOT NULL,
    drug_code VARCHAR(50) NOT NULL,
    dosage VARCHAR(200) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    instructions TEXT,
    side_effects VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PRESCRIBED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

CREATE TABLE lab_order (
    lab_order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    test_name VARCHAR(100) NOT NULL,
    test_code VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    clinical_info TEXT,
    instructions TEXT,
    priority VARCHAR(50) NOT NULL DEFAULT 'ROUTINE',
    status VARCHAR(50) NOT NULL DEFAULT 'ORDERED',
    scheduled_at TIMESTAMP,
    collected_at TIMESTAMP,
    completed_at TIMESTAMP,
    result TEXT,
    interpretation TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

-- Indexes for EMR tables
CREATE INDEX idx_medical_record_patient_id ON medical_record(patient_id);
CREATE INDEX idx_medical_record_doctor_id ON medical_record(doctor_id);
CREATE INDEX idx_medical_record_appointment_id ON medical_record(appointment_id);
CREATE INDEX idx_medical_record_status ON medical_record(status);

CREATE INDEX idx_chart_template_category ON chart_template(category);
CREATE INDEX idx_chart_template_code ON chart_template(code);
CREATE INDEX idx_chart_template_active ON chart_template(active);

CREATE INDEX idx_prescription_patient_id ON prescription(patient_id);
CREATE INDEX idx_prescription_doctor_id ON prescription(doctor_id);
CREATE INDEX idx_prescription_appointment_id ON prescription(appointment_id);
CREATE INDEX idx_prescription_drug_code ON prescription(drug_code);
CREATE INDEX idx_prescription_status ON prescription(status);

CREATE INDEX idx_lab_order_patient_id ON lab_order(patient_id);
CREATE INDEX idx_lab_order_doctor_id ON lab_order(doctor_id);
CREATE INDEX idx_lab_order_appointment_id ON lab_order(appointment_id);
CREATE INDEX idx_lab_order_test_code ON lab_order(test_code);
CREATE INDEX idx_lab_order_status ON lab_order(status);
CREATE INDEX idx_lab_order_scheduled_at ON lab_order(scheduled_at);
