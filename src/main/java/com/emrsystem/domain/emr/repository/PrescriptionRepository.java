package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Page<Prescription> findByMedicalRecord_MedicalRecordId(Long medicalRecordId, Pageable pageable);
    
    // Prescription history queries
    List<Prescription> findByPatient_PatientId(Long patientId);
    Page<Prescription> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<Prescription> findByPatient_PatientIdAndCreatedAtBetween(Long patientId, LocalDateTime start, LocalDateTime end);
    List<Prescription> findByPatient_PatientIdAndDrugCode(Long patientId, String drugCode);
    
    @Query("SELECT p FROM Prescription p WHERE p.patient.patientId = :patientId " +
           "AND p.drugCode = :drugCode " +
           "AND p.status != 'CANCELLED' " +
           "AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Prescription> findActivePrescriptionsByPatientAndDrug(@Param("patientId") Long patientId,
                                                                @Param("drugCode") String drugCode,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);
}