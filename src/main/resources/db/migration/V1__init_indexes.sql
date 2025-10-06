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

