-- ============================================================================
-- MediPlug EMR System - 통합 데이터베이스 스키마
-- 모든 마이그레이션 파일을 하나로 통합한 완전한 스키마 정의
-- ============================================================================

-- ============================================================================
-- 1. 기본 제약 조건 및 인덱스 (V1)
-- ============================================================================

-- Unique constraints
ALTER TABLE hospital ADD CONSTRAINT uk_hospital_name UNIQUE (name);
ALTER TABLE doctor ADD CONSTRAINT uk_doctor_license UNIQUE (license_number);
ALTER TABLE patient ADD CONSTRAINT uk_patient_identifier UNIQUE (identifier);

-- Indexes for search
CREATE INDEX idx_hospital_name ON hospital (name);
CREATE INDEX idx_department_hospital ON department (hospital_id);
CREATE INDEX idx_department_name ON department (name);
CREATE INDEX idx_doctor_department ON doctor (department_id);
CREATE INDEX idx_doctor_name ON doctor (name);
CREATE INDEX idx_patient_name ON patient (name);
CREATE INDEX idx_patient_identifier ON patient (identifier);
CREATE INDEX idx_appt_doctor ON appointment (doctor_id);
CREATE INDEX idx_appt_patient ON appointment (patient_id);
CREATE INDEX idx_appt_start_end ON appointment (start_at, end_at);

-- ============================================================================
-- 2. EMR (전자의무기록) 테이블 (V2)
-- ============================================================================

-- Medical Record
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

-- Chart Template
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

-- Prescription
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

-- Lab Order
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

-- ============================================================================
-- 3. Billing (청구) 테이블 (V3)
-- ============================================================================

-- Procedure Code
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

-- Billing
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

-- Receipt
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

-- ============================================================================
-- 4. Pharmacy (약국) 테이블 (V4)
-- ============================================================================

-- Drug Master
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

-- Drug Inventory
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

-- ============================================================================
-- 5. Bed Management (병상 관리) 테이블 (V5)
-- ============================================================================

-- Bed
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

-- Admission
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

-- ============================================================================
-- 6. Patient Allergy (환자 알레르기) 테이블 (V6)
-- ============================================================================

CREATE TABLE patient_allergy (
    allergy_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    allergen_type VARCHAR(50) NOT NULL,
    allergen_name VARCHAR(200) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    reaction TEXT,
    diagnosed_date DATE,
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id)
);

-- Indexes for Patient Allergy
CREATE INDEX idx_patient_allergy_patient_id ON patient_allergy(patient_id);
CREATE INDEX idx_patient_allergy_allergen_type ON patient_allergy(allergen_type);
CREATE INDEX idx_patient_allergy_active ON patient_allergy(active);
CREATE INDEX idx_patient_allergy_allergen_name ON patient_allergy(allergen_name);

-- ============================================================================
-- 7. Diagnosis Code (진단 코드) 테이블 (V7)
-- ============================================================================

CREATE TABLE diagnosis_code (
    diagnosis_code_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(500) NOT NULL,
    category VARCHAR(100),
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 10,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for Diagnosis Code
CREATE INDEX idx_diagnosis_code_code ON diagnosis_code(code);
CREATE INDEX idx_diagnosis_code_name ON diagnosis_code(name);
CREATE INDEX idx_diagnosis_code_category ON diagnosis_code(category);
CREATE INDEX idx_diagnosis_code_active ON diagnosis_code(active);

-- ============================================================================
-- 8. Drug Interaction (약물 상호작용) 테이블 (V8)
-- ============================================================================

CREATE TABLE drug_interaction (
    interaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drug1_id BIGINT NOT NULL,
    drug2_id BIGINT NOT NULL,
    severity VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    clinical_significance TEXT,
    management TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (drug1_id) REFERENCES drug_master(drug_master_id),
    FOREIGN KEY (drug2_id) REFERENCES drug_master(drug_master_id),
    CONSTRAINT chk_different_drugs CHECK (drug1_id != drug2_id)
);

-- Indexes for Drug Interaction
CREATE INDEX idx_drug_interaction_drug1_id ON drug_interaction(drug1_id);
CREATE INDEX idx_drug_interaction_drug2_id ON drug_interaction(drug2_id);
CREATE INDEX idx_drug_interaction_severity ON drug_interaction(severity);
CREATE INDEX idx_drug_interaction_active ON drug_interaction(active);
CREATE UNIQUE INDEX idx_drug_interaction_unique ON drug_interaction(
    LEAST(drug1_id, drug2_id), 
    GREATEST(drug1_id, drug2_id)
);

-- ============================================================================
-- 9. Lab Result (검사 결과) 테이블 (V9)
-- ============================================================================

CREATE TABLE lab_result (
    lab_result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lab_order_id BIGINT NOT NULL,
    test_item_name VARCHAR(200) NOT NULL,
    test_item_code VARCHAR(50),
    result_value VARCHAR(200),
    unit VARCHAR(50),
    reference_range VARCHAR(100),
    abnormal_flag VARCHAR(10),
    status VARCHAR(50) NOT NULL DEFAULT 'FINAL',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lab_order_id) REFERENCES lab_order(lab_order_id)
);

-- Indexes for Lab Result
CREATE INDEX idx_lab_result_lab_order_id ON lab_result(lab_order_id);
CREATE INDEX idx_lab_result_status ON lab_result(status);
CREATE INDEX idx_lab_result_abnormal_flag ON lab_result(abnormal_flag);
CREATE INDEX idx_lab_result_test_item_code ON lab_result(test_item_code);

-- ============================================================================
-- 10. Medical Image (의료 영상) 테이블 (V10)
-- ============================================================================

CREATE TABLE medical_image (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    medical_record_id BIGINT,
    lab_order_id BIGINT,
    image_type VARCHAR(50) NOT NULL,
    modality VARCHAR(50),
    file_path VARCHAR(500) NOT NULL,
    file_name VARCHAR(200) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    study_date TIMESTAMP NOT NULL,
    study_description VARCHAR(500),
    interpretation TEXT,
    radiologist_id BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'UPLOADED',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id),
    FOREIGN KEY (medical_record_id) REFERENCES medical_record(medical_record_id),
    FOREIGN KEY (lab_order_id) REFERENCES lab_order(lab_order_id),
    FOREIGN KEY (radiologist_id) REFERENCES doctor(doctor_id)
);

-- Indexes for Medical Image
CREATE INDEX idx_medical_image_patient_id ON medical_image(patient_id);
CREATE INDEX idx_medical_image_medical_record_id ON medical_image(medical_record_id);
CREATE INDEX idx_medical_image_lab_order_id ON medical_image(lab_order_id);
CREATE INDEX idx_medical_image_image_type ON medical_image(image_type);
CREATE INDEX idx_medical_image_status ON medical_image(status);
CREATE INDEX idx_medical_image_study_date ON medical_image(study_date);

-- ============================================================================
-- 11. Attachment (첨부파일) 테이블 (V11)
-- ============================================================================

CREATE TABLE attachment (
    attachment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    file_name VARCHAR(200) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    category VARCHAR(50),
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for Attachment
CREATE INDEX idx_attachment_entity_type_id ON attachment(entity_type, entity_id);
CREATE INDEX idx_attachment_category ON attachment(category);
CREATE INDEX idx_attachment_active ON attachment(active);

-- ============================================================================
-- 12. Medical Certificate (진단서) 테이블 (V12)
-- ============================================================================

CREATE TABLE medical_certificate (
    certificate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    medical_record_id BIGINT,
    doctor_id BIGINT NOT NULL,
    certificate_type VARCHAR(50) NOT NULL,
    certificate_number VARCHAR(100) NOT NULL UNIQUE,
    issue_date DATE NOT NULL,
    effective_date DATE NOT NULL,
    expiry_date DATE,
    content TEXT NOT NULL,
    diagnosis TEXT,
    purpose TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'ISSUED',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id),
    FOREIGN KEY (medical_record_id) REFERENCES medical_record(medical_record_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

-- Indexes for Medical Certificate
CREATE INDEX idx_medical_certificate_patient_id ON medical_certificate(patient_id);
CREATE INDEX idx_medical_certificate_certificate_number ON medical_certificate(certificate_number);
CREATE INDEX idx_medical_certificate_certificate_type ON medical_certificate(certificate_type);
CREATE INDEX idx_medical_certificate_status ON medical_certificate(status);
CREATE INDEX idx_medical_certificate_issue_date ON medical_certificate(issue_date);

-- ============================================================================
-- 13. User Account 업데이트 (V13)
-- ============================================================================

ALTER TABLE user_account 
ADD COLUMN name VARCHAR(100),
ADD COLUMN email VARCHAR(100),
ADD COLUMN phone VARCHAR(30),
ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN locked BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Indexes for User Account
CREATE INDEX idx_user_account_active ON user_account(active);
CREATE INDEX idx_user_account_locked ON user_account(locked);

-- ============================================================================
-- 14. Surgery (수술) 테이블 (V14)
-- ============================================================================

-- Surgery
CREATE TABLE surgery (
    surgery_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    surgery_name VARCHAR(200) NOT NULL,
    surgery_code VARCHAR(50),
    scheduled_date_time TIMESTAMP NOT NULL,
    actual_start_date_time TIMESTAMP,
    actual_end_date_time TIMESTAMP,
    surgeon_id BIGINT NOT NULL,
    anesthesiologist_id BIGINT,
    anesthesia_type VARCHAR(50),
    surgery_room VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    pre_op_diagnosis TEXT,
    post_op_diagnosis TEXT,
    procedure TEXT,
    findings TEXT,
    complications TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id),
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id),
    FOREIGN KEY (surgeon_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (anesthesiologist_id) REFERENCES doctor(doctor_id)
);

-- Surgery Team
CREATE TABLE surgery_team (
    surgery_team_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    surgery_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (surgery_id) REFERENCES surgery(surgery_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

-- Pre-op Checklist
CREATE TABLE pre_op_checklist (
    checklist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    surgery_id BIGINT NOT NULL,
    item VARCHAR(200) NOT NULL,
    checked BOOLEAN NOT NULL DEFAULT FALSE,
    checked_by VARCHAR(200),
    checked_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (surgery_id) REFERENCES surgery(surgery_id)
);

-- Indexes for Surgery
CREATE INDEX idx_surgery_patient_id ON surgery(patient_id);
CREATE INDEX idx_surgery_surgeon_id ON surgery(surgeon_id);
CREATE INDEX idx_surgery_scheduled_date_time ON surgery(scheduled_date_time);
CREATE INDEX idx_surgery_status ON surgery(status);
CREATE INDEX idx_surgery_room ON surgery(surgery_room);
CREATE INDEX idx_surgery_team_surgery_id ON surgery_team(surgery_id);
CREATE INDEX idx_surgery_team_doctor_id ON surgery_team(doctor_id);
CREATE INDEX idx_pre_op_checklist_surgery_id ON pre_op_checklist(surgery_id);

-- ============================================================================
-- 15. Infection Control (감염 관리) 테이블 (V15)
-- ============================================================================

-- Infection Case
CREATE TABLE infection_case (
    infection_case_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    infection_type VARCHAR(100) NOT NULL,
    infection_name VARCHAR(200) NOT NULL,
    diagnosed_date DATE NOT NULL,
    resolved_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    severity VARCHAR(50) NOT NULL,
    isolation_room VARCHAR(100),
    symptoms TEXT,
    treatment TEXT,
    prevention_measures TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id)
);

-- Infection Prevention
CREATE TABLE infection_prevention (
    prevention_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    infection_case_id BIGINT NOT NULL,
    measure VARCHAR(200) NOT NULL,
    implemented_at TIMESTAMP NOT NULL,
    implemented_by VARCHAR(200),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (infection_case_id) REFERENCES infection_case(infection_case_id)
);

-- Indexes for Infection
CREATE INDEX idx_infection_case_patient_id ON infection_case(patient_id);
CREATE INDEX idx_infection_case_status ON infection_case(status);
CREATE INDEX idx_infection_case_infection_type ON infection_case(infection_type);
CREATE INDEX idx_infection_case_diagnosed_date ON infection_case(diagnosed_date);
CREATE INDEX idx_infection_case_isolation_room ON infection_case(isolation_room);
CREATE INDEX idx_infection_prevention_case_id ON infection_prevention(infection_case_id);
CREATE INDEX idx_infection_prevention_active ON infection_prevention(active);

-- ============================================================================
-- 16. Blood Type and Transfusion (혈액형 및 수혈) 테이블 (V16)
-- ============================================================================

-- Blood Type
CREATE TABLE blood_type (
    blood_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL UNIQUE,
    abo_type VARCHAR(10) NOT NULL,
    rh_type VARCHAR(10) NOT NULL,
    tested_at VARCHAR(200),
    tested_date DATE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id)
);

-- Transfusion Request
CREATE TABLE transfusion_request (
    transfusion_request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    requesting_doctor_id BIGINT NOT NULL,
    blood_component VARCHAR(50) NOT NULL,
    units INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    approving_doctor_id BIGINT,
    approved_at TIMESTAMP,
    reason TEXT NOT NULL,
    rejection_reason TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id),
    FOREIGN KEY (requesting_doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (approving_doctor_id) REFERENCES doctor(doctor_id)
);

-- Transfusion Record
CREATE TABLE transfusion_record (
    transfusion_record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfusion_request_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    administering_doctor_id BIGINT,
    blood_component VARCHAR(50) NOT NULL,
    blood_type VARCHAR(50) NOT NULL,
    blood_bag_number VARCHAR(100) NOT NULL,
    units INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'IN_PROGRESS',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transfusion_request_id) REFERENCES transfusion_request(transfusion_request_id),
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id),
    FOREIGN KEY (administering_doctor_id) REFERENCES doctor(doctor_id)
);

-- Transfusion Reaction
CREATE TABLE transfusion_reaction (
    reaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfusion_record_id BIGINT NOT NULL,
    reaction_type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    symptoms TEXT NOT NULL,
    treatment TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transfusion_record_id) REFERENCES transfusion_record(transfusion_record_id)
);

-- Indexes
CREATE INDEX idx_blood_type_patient_id ON blood_type(patient_id);
CREATE INDEX idx_transfusion_request_patient_id ON transfusion_request(patient_id);
CREATE INDEX idx_transfusion_request_status ON transfusion_request(status);
CREATE INDEX idx_transfusion_record_patient_id ON transfusion_record(patient_id);
CREATE INDEX idx_transfusion_record_request_id ON transfusion_record(transfusion_request_id);
CREATE INDEX idx_transfusion_reaction_record_id ON transfusion_reaction(transfusion_record_id);

-- ============================================================================
-- 17. Vital Signs (생체징후) 테이블 (V17)
-- ============================================================================

-- Vital Sign
CREATE TABLE vital_sign (
    vital_sign_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    measured_at TIMESTAMP NOT NULL,
    systolic_bp DECIMAL(5,2),
    diastolic_bp DECIMAL(5,2),
    heart_rate DECIMAL(5,2),
    temperature DECIMAL(5,2),
    respiratory_rate DECIMAL(5,2),
    oxygen_saturation DECIMAL(5,2),
    blood_sugar DECIMAL(5,2),
    pain_score DECIMAL(5,2),
    measured_by VARCHAR(50),
    device VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id)
);

-- Vital Sign Alert
CREATE TABLE vital_sign_alert (
    alert_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vital_sign_id BIGINT NOT NULL,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_by VARCHAR(200),
    acknowledged_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (vital_sign_id) REFERENCES vital_sign(vital_sign_id)
);

-- Indexes for Vital Signs
CREATE INDEX idx_vital_sign_patient_id ON vital_sign(patient_id);
CREATE INDEX idx_vital_sign_measured_at ON vital_sign(measured_at);
CREATE INDEX idx_vital_sign_alert_vital_sign_id ON vital_sign_alert(vital_sign_id);
CREATE INDEX idx_vital_sign_alert_acknowledged ON vital_sign_alert(acknowledged);
CREATE INDEX idx_vital_sign_alert_severity ON vital_sign_alert(severity);

-- ============================================================================
-- 18. Inpatient Management (입원 관리) 테이블 (V18)
-- ============================================================================

-- Inpatient Daily Record
CREATE TABLE inpatient_daily_record (
    daily_record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admission_id BIGINT NOT NULL,
    record_date DATE NOT NULL,
    doctor_id BIGINT,
    condition TEXT,
    treatment TEXT,
    plan TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (admission_id) REFERENCES admission(admission_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

-- Nursing Note
CREATE TABLE nursing_note (
    nursing_note_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admission_id BIGINT NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    recorded_by VARCHAR(200) NOT NULL,
    note_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (admission_id) REFERENCES admission(admission_id)
);

-- Indexes for Inpatient Management
CREATE INDEX idx_inpatient_daily_record_admission_id ON inpatient_daily_record(admission_id);
CREATE INDEX idx_inpatient_daily_record_record_date ON inpatient_daily_record(record_date);
CREATE INDEX idx_nursing_note_admission_id ON nursing_note(admission_id);
CREATE INDEX idx_nursing_note_recorded_at ON nursing_note(recorded_at);
CREATE INDEX idx_nursing_note_note_type ON nursing_note(note_type);

-- ============================================================================
-- 19. Prescription History (처방전 이력) 테이블 (V19)
-- ============================================================================

-- Prescription History
CREATE TABLE prescription_history (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    changed_field VARCHAR(200),
    old_value VARCHAR(500),
    new_value VARCHAR(500),
    changed_by VARCHAR(200),
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id)
);

-- Medication Log
CREATE TABLE medication_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    medication_date DATE NOT NULL,
    medication_time TIMESTAMP NOT NULL,
    taken BOOLEAN NOT NULL DEFAULT FALSE,
    recorded_by VARCHAR(200),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id),
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id)
);

-- Indexes for Prescription History
CREATE INDEX idx_prescription_history_prescription_id ON prescription_history(prescription_id);
CREATE INDEX idx_prescription_history_action_type ON prescription_history(action_type);
CREATE INDEX idx_medication_log_prescription_id ON medication_log(prescription_id);
CREATE INDEX idx_medication_log_patient_id ON medication_log(patient_id);
CREATE INDEX idx_medication_log_medication_date ON medication_log(medication_date);
CREATE INDEX idx_medication_log_taken ON medication_log(taken);

-- ============================================================================
-- 20. Appointment Enhancement (예약 확장) 테이블 (V20)
-- ============================================================================

-- Appointment Waitlist
CREATE TABLE appointment_waitlist (
    waitlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    preferred_start_at TIMESTAMP NOT NULL,
    preferred_end_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    priority INT NOT NULL DEFAULT 10,
    reason TEXT,
    notes TEXT,
    appointment_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES TBL_PATIENT(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id)
);

-- Appointment History
CREATE TABLE appointment_history (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    changed_field VARCHAR(200),
    old_value VARCHAR(500),
    new_value VARCHAR(500),
    changed_by VARCHAR(200),
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id)
);

-- Appointment Reminder
CREATE TABLE appointment_reminder (
    reminder_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    recipient VARCHAR(500),
    message TEXT,
    error_message TEXT,
    retry_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id)
);

-- Indexes for Appointment Enhancement
CREATE INDEX idx_appointment_waitlist_patient_id ON appointment_waitlist(patient_id);
CREATE INDEX idx_appointment_waitlist_doctor_id ON appointment_waitlist(doctor_id);
CREATE INDEX idx_appointment_waitlist_status ON appointment_waitlist(status);
CREATE INDEX idx_appointment_waitlist_priority ON appointment_waitlist(priority);
CREATE INDEX idx_appointment_history_appointment_id ON appointment_history(appointment_id);
CREATE INDEX idx_appointment_history_action_type ON appointment_history(action_type);
CREATE INDEX idx_appointment_reminder_appointment_id ON appointment_reminder(appointment_id);
CREATE INDEX idx_appointment_reminder_status ON appointment_reminder(status);
CREATE INDEX idx_appointment_reminder_scheduled_at ON appointment_reminder(scheduled_at);

-- ============================================================================
-- 21. Inventory Management (재고 관리) 테이블 (V21)
-- ============================================================================

-- Supplier
CREATE TABLE supplier (
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(50),
    email VARCHAR(100),
    address VARCHAR(200),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Inventory Threshold
CREATE TABLE inventory_threshold (
    threshold_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drug_master_id BIGINT NOT NULL,
    minimum_quantity INT NOT NULL,
    reorder_quantity INT NOT NULL,
    auto_reorder_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (drug_master_id) REFERENCES drug_master(drug_master_id),
    UNIQUE KEY uk_drug_master_id (drug_master_id)
);

-- Purchase Order
CREATE TABLE purchase_order (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    drug_master_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    expected_delivery_date DATE,
    actual_delivery_date DATE,
    ordered_by VARCHAR(200),
    approved_by VARCHAR(200),
    ordered_at TIMESTAMP,
    approved_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (drug_master_id) REFERENCES drug_master(drug_master_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);

-- Indexes
CREATE INDEX idx_supplier_status ON supplier(status);
CREATE INDEX idx_inventory_threshold_drug_master_id ON inventory_threshold(drug_master_id);
CREATE INDEX idx_inventory_threshold_auto_reorder ON inventory_threshold(auto_reorder_enabled);
CREATE INDEX idx_purchase_order_status ON purchase_order(status);
CREATE INDEX idx_purchase_order_drug_master_id ON purchase_order(drug_master_id);
CREATE INDEX idx_purchase_order_supplier_id ON purchase_order(supplier_id);
CREATE INDEX idx_purchase_order_expected_delivery_date ON purchase_order(expected_delivery_date);

-- ============================================================================
-- 22. Schedule Management (스케줄 관리) 테이블 (V22)
-- ============================================================================

-- Shift
CREATE TABLE shift (
    shift_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shift_type VARCHAR(50) NOT NULL UNIQUE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration_hours INT NOT NULL,
    description VARCHAR(200),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Staff Schedule
CREATE TABLE staff_schedule (
    schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT,
    staff_type VARCHAR(50) NOT NULL,
    staff_name VARCHAR(200),
    schedule_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    shift_type VARCHAR(50) NOT NULL,
    department VARCHAR(200),
    location VARCHAR(200),
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

-- Indexes
CREATE INDEX idx_staff_schedule_doctor_id ON staff_schedule(doctor_id);
CREATE INDEX idx_staff_schedule_schedule_date ON staff_schedule(schedule_date);
CREATE INDEX idx_staff_schedule_staff_type ON staff_schedule(staff_type);
CREATE INDEX idx_staff_schedule_shift_type ON staff_schedule(shift_type);
CREATE INDEX idx_staff_schedule_status ON staff_schedule(status);

-- ============================================================================
-- 23. Two Factor Authentication (2단계 인증) 테이블 (V23)
-- ============================================================================

CREATE TABLE two_factor_auth (
    two_factor_auth_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_account_id BIGINT NOT NULL UNIQUE,
    secret_key VARCHAR(200) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    backup_codes VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_account_id) REFERENCES user_account(user_account_id)
);

CREATE INDEX idx_two_factor_auth_user_account_id ON two_factor_auth(user_account_id);

-- ============================================================================
-- 24. Document Template (문서 템플릿) 테이블 (V24)
-- ============================================================================

-- Document Template
CREATE TABLE document_template (
    template_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    format VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    version VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Template Variable
CREATE TABLE template_variable (
    variable_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id BIGINT NOT NULL,
    variable_name VARCHAR(100) NOT NULL,
    display_name VARCHAR(200),
    data_type VARCHAR(50),
    default_value TEXT,
    description TEXT,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    validation_rule VARCHAR(500),
    FOREIGN KEY (template_id) REFERENCES document_template(template_id) ON DELETE CASCADE
);

-- Indexes for Document Template
CREATE INDEX idx_document_template_document_type ON document_template(document_type);
CREATE INDEX idx_document_template_active ON document_template(active);
CREATE INDEX idx_document_template_is_default ON document_template(is_default);
CREATE INDEX idx_document_template_format ON document_template(format);
CREATE INDEX idx_template_variable_template_id ON template_variable(template_id);
CREATE INDEX idx_template_variable_variable_name ON template_variable(variable_name);

-- ============================================================================
-- 25. Document Template Advanced Features (문서 템플릿 고급 기능) 테이블 (V25)
-- ============================================================================

-- Template Version Management
CREATE TABLE template_version (
    version_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id BIGINT NOT NULL,
    version VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    change_description VARCHAR(500),
    created_by_user_id BIGINT,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES document_template(template_id) ON DELETE CASCADE,
    FOREIGN KEY (created_by_user_id) REFERENCES user_account(user_id) ON DELETE SET NULL,
    UNIQUE KEY uk_template_version (template_id, version)
);

-- Template Translation (다국어 지원)
CREATE TABLE template_translation (
    translation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id BIGINT NOT NULL,
    language_code VARCHAR(10) NOT NULL,
    template_name VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES document_template(template_id) ON DELETE CASCADE,
    UNIQUE KEY uk_template_translation (template_id, language_code)
);

-- Hospital Template (병원별 템플릿 공유)
CREATE TABLE hospital_template (
    hospital_template_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id BIGINT NOT NULL,
    hospital_id BIGINT NOT NULL,
    shared BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES document_template(template_id) ON DELETE CASCADE,
    FOREIGN KEY (hospital_id) REFERENCES hospital(hospital_id) ON DELETE CASCADE,
    UNIQUE KEY uk_hospital_template (template_id, hospital_id)
);

-- Template Asset (이미지, 차트, 로고 등)
CREATE TABLE template_asset (
    asset_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id BIGINT,
    asset_name VARCHAR(200) NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    mime_type VARCHAR(100),
    file_size BIGINT,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES document_template(template_id) ON DELETE SET NULL
);

-- Indexes for Document Template Advanced Features
CREATE INDEX idx_template_version_template_id ON template_version(template_id);
CREATE INDEX idx_template_version_created_at ON template_version(created_at DESC);
CREATE INDEX idx_template_translation_template_id ON template_translation(template_id);
CREATE INDEX idx_template_translation_language_code ON template_translation(language_code);
CREATE INDEX idx_hospital_template_template_id ON hospital_template(template_id);
CREATE INDEX idx_hospital_template_hospital_id ON hospital_template(hospital_id);
CREATE INDEX idx_hospital_template_shared ON hospital_template(shared, active);
CREATE INDEX idx_template_asset_template_id ON template_asset(template_id);
CREATE INDEX idx_template_asset_asset_type ON template_asset(asset_type, active);
CREATE INDEX idx_template_asset_active ON template_asset(active);

-- ============================================================================
-- 통합 스키마 완료
-- ============================================================================

