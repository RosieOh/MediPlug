package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Page<MedicalRecord> findByPatient_PatientId(Long patientId, Pageable pageable);
    Page<MedicalRecord> findByDoctor_DoctorId(Long doctorId, Pageable pageable);
    
    // Statistics queries
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}