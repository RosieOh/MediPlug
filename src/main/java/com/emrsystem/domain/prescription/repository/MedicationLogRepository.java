package com.emrsystem.domain.prescription.repository;

import com.emrsystem.domain.prescription.entity.MedicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long> {
    List<MedicationLog> findByPrescription_PrescriptionId(Long prescriptionId);
    List<MedicationLog> findByPatient_PatientId(Long patientId);
    List<MedicationLog> findByPatient_PatientIdAndMedicationDateBetween(Long patientId, LocalDate startDate, LocalDate endDate);
    List<MedicationLog> findByPrescription_PrescriptionIdAndMedicationDateBetween(Long prescriptionId, LocalDate startDate, LocalDate endDate);
    List<MedicationLog> findByPatient_PatientIdAndTaken(Long patientId, boolean taken);
}

