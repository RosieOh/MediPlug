package com.emrsystem.domain.prescription.facade;

import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.prescription.entity.MedicationLog;
import com.emrsystem.domain.prescription.entity.PrescriptionHistory;
import com.emrsystem.domain.prescription.request.PrescriptionHistoryRequests;
import com.emrsystem.domain.prescription.response.PrescriptionHistoryResponses;
import com.emrsystem.domain.prescription.service.PrescriptionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrescriptionHistoryFacade {

    private final PrescriptionHistoryService prescriptionHistoryService;

    public Page<PrescriptionHistoryResponses.PrescriptionSummary> getPrescriptionHistoryByPatient(
            Long patientId, Pageable pageable) {
        return prescriptionHistoryService.getPrescriptionHistoryByPatient(patientId, pageable);
    }

    public List<PrescriptionHistoryResponses.PrescriptionHistorySummary> getPrescriptionChangeHistory(Long prescriptionId) {
        return prescriptionHistoryService.getPrescriptionChangeHistory(prescriptionId);
    }

    public PrescriptionHistoryResponses.DuplicatePrescriptionCheck checkDuplicatePrescription(
            Long patientId, String drugCode, LocalDateTime startDate, LocalDateTime endDate) {
        return prescriptionHistoryService.checkDuplicatePrescription(patientId, drugCode, startDate, endDate);
    }

    public List<PrescriptionHistoryResponses.MedicationLogSummary> getMedicationLogsByPatient(
            Long patientId, LocalDate startDate, LocalDate endDate) {
        return prescriptionHistoryService.getMedicationLogsByPatient(patientId, startDate, endDate);
    }

    public List<PrescriptionHistoryResponses.MedicationLogSummary> getMedicationLogsByPrescription(
            Long prescriptionId, LocalDate startDate, LocalDate endDate) {
        return prescriptionHistoryService.getMedicationLogsByPrescription(prescriptionId, startDate, endDate);
    }

    public MedicationLog createMedicationLog(PrescriptionHistoryRequests.CreateMedicationLogRequest request) {
        return prescriptionHistoryService.createMedicationLog(request);
    }

    public MedicationLog updateMedicationLog(Long logId, PrescriptionHistoryRequests.UpdateMedicationLogRequest request) {
        return prescriptionHistoryService.updateMedicationLog(logId, request);
    }

    public PrescriptionHistory recordPrescriptionHistory(Prescription prescription, String actionType, String changedBy) {
        return prescriptionHistoryService.recordPrescriptionHistory(prescription, actionType, changedBy);
    }

    public PrescriptionHistory recordPrescriptionChange(Prescription prescription, String changedField,
                                                       String oldValue, String newValue, String changedBy, String reason) {
        return prescriptionHistoryService.recordPrescriptionChange(prescription, changedField, oldValue, newValue, changedBy, reason);
    }
}

