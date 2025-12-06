package com.emrsystem.domain.prescription.store;

import com.emrsystem.domain.prescription.entity.MedicationLog;
import com.emrsystem.domain.prescription.entity.PrescriptionHistory;
import com.emrsystem.domain.prescription.repository.MedicationLogRepository;
import com.emrsystem.domain.prescription.repository.PrescriptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PrescriptionHistoryStore {

    private final PrescriptionHistoryRepository prescriptionHistoryRepository;
    private final MedicationLogRepository medicationLogRepository;

    // PrescriptionHistory operations
    public PrescriptionHistory savePrescriptionHistory(PrescriptionHistory history) {
        return prescriptionHistoryRepository.save(history);
    }

    public Optional<PrescriptionHistory> findPrescriptionHistoryById(Long id) {
        return prescriptionHistoryRepository.findById(id);
    }

    public List<PrescriptionHistory> findPrescriptionHistoriesByPrescription(Long prescriptionId) {
        return prescriptionHistoryRepository.findByPrescription_PrescriptionIdOrderByCreatedAtDesc(prescriptionId);
    }

    public List<PrescriptionHistory> findPrescriptionHistoriesByActionType(String actionType) {
        return prescriptionHistoryRepository.findByActionType(actionType);
    }

    // MedicationLog operations
    public MedicationLog saveMedicationLog(MedicationLog log) {
        return medicationLogRepository.save(log);
    }

    public Optional<MedicationLog> findMedicationLogById(Long id) {
        return medicationLogRepository.findById(id);
    }

    public List<MedicationLog> findMedicationLogsByPrescription(Long prescriptionId) {
        return medicationLogRepository.findByPrescription_PrescriptionId(prescriptionId);
    }

    public List<MedicationLog> findMedicationLogsByPatient(Long patientId) {
        return medicationLogRepository.findByPatient_PatientId(patientId);
    }

    public List<MedicationLog> findMedicationLogsByPatientAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate) {
        return medicationLogRepository.findByPatient_PatientIdAndMedicationDateBetween(patientId, startDate, endDate);
    }

    public List<MedicationLog> findMedicationLogsByPrescriptionAndDateRange(Long prescriptionId, LocalDate startDate, LocalDate endDate) {
        return medicationLogRepository.findByPrescription_PrescriptionIdAndMedicationDateBetween(prescriptionId, startDate, endDate);
    }

    public List<MedicationLog> findMedicationLogsByPatientAndTaken(Long patientId, boolean taken) {
        return medicationLogRepository.findByPatient_PatientIdAndTaken(patientId, taken);
    }
}

