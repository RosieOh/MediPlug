package com.emrsystem.domain.prescription.service;

import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.emr.repository.PrescriptionRepository;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.prescription.entity.MedicationLog;
import com.emrsystem.domain.prescription.entity.PrescriptionHistory;
import com.emrsystem.domain.prescription.request.PrescriptionHistoryRequests;
import com.emrsystem.domain.prescription.response.PrescriptionHistoryResponses;
import com.emrsystem.domain.prescription.store.PrescriptionHistoryStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionHistoryService {

    private final PrescriptionHistoryStore prescriptionHistoryStore;
    private final PrescriptionRepository prescriptionRepository;
    private final PatientStore patientStore;

    /**
     * 환자별 처방 이력 조회
     */
    public Page<PrescriptionHistoryResponses.PrescriptionSummary> getPrescriptionHistoryByPatient(
            Long patientId, Pageable pageable) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Page<Prescription> prescriptions = prescriptionRepository.findByPatient_PatientId(patientId, pageable);
        return prescriptions.map(PrescriptionHistoryResponses.PrescriptionSummary::of);
    }

    /**
     * 처방별 변경 이력 조회
     */
    public List<PrescriptionHistoryResponses.PrescriptionHistorySummary> getPrescriptionChangeHistory(Long prescriptionId) {
        List<PrescriptionHistory> histories = prescriptionHistoryStore.findPrescriptionHistoriesByPrescription(prescriptionId);
        return histories.stream()
                .map(PrescriptionHistoryResponses.PrescriptionHistorySummary::of)
                .collect(Collectors.toList());
    }

    /**
     * 약물 중복 처방 체크
     */
    public PrescriptionHistoryResponses.DuplicatePrescriptionCheck checkDuplicatePrescription(
            Long patientId, String drugCode, LocalDateTime startDate, LocalDateTime endDate) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        List<Prescription> activePrescriptions = prescriptionRepository.findActivePrescriptionsByPatientAndDrug(
                patientId, drugCode, startDate, endDate);

        boolean hasDuplicate = activePrescriptions.size() > 1;

        return PrescriptionHistoryResponses.DuplicatePrescriptionCheck.builder()
                .patientId(patientId)
                .patientName(patient.getName())
                .drugCode(drugCode)
                .hasDuplicate(hasDuplicate)
                .duplicateCount(activePrescriptions.size())
                .prescriptions(activePrescriptions.stream()
                        .map(PrescriptionHistoryResponses.PrescriptionSummary::of)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 약물 복용 이력 조회
     */
    public List<PrescriptionHistoryResponses.MedicationLogSummary> getMedicationLogsByPatient(
            Long patientId, LocalDate startDate, LocalDate endDate) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        List<MedicationLog> logs = prescriptionHistoryStore.findMedicationLogsByPatientAndDateRange(
                patientId, startDate, endDate);

        return logs.stream()
                .map(PrescriptionHistoryResponses.MedicationLogSummary::of)
                .collect(Collectors.toList());
    }

    /**
     * 처방별 약물 복용 이력 조회
     */
    public List<PrescriptionHistoryResponses.MedicationLogSummary> getMedicationLogsByPrescription(
            Long prescriptionId, LocalDate startDate, LocalDate endDate) {
        List<MedicationLog> logs = prescriptionHistoryStore.findMedicationLogsByPrescriptionAndDateRange(
                prescriptionId, startDate, endDate);

        return logs.stream()
                .map(PrescriptionHistoryResponses.MedicationLogSummary::of)
                .collect(Collectors.toList());
    }

    /**
     * 약물 복용 기록 생성
     */
    @Transactional
    public MedicationLog createMedicationLog(PrescriptionHistoryRequests.CreateMedicationLogRequest request) {
        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "처방을 찾을 수 없습니다."));

        Patient patient = prescription.getPatient();

        MedicationLog log = MedicationLog.create(
                prescription,
                patient,
                request.getMedicationDate() != null ? request.getMedicationDate() : LocalDate.now(),
                request.getMedicationTime() != null ? request.getMedicationTime() : LocalDateTime.now(),
                request.isTaken(),
                request.getRecordedBy()
        );

        return prescriptionHistoryStore.saveMedicationLog(log);
    }

    /**
     * 약물 복용 기록 업데이트
     */
    @Transactional
    public MedicationLog updateMedicationLog(Long logId, PrescriptionHistoryRequests.UpdateMedicationLogRequest request) {
        MedicationLog log = prescriptionHistoryStore.findMedicationLogById(logId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "복용 기록을 찾을 수 없습니다."));

        if (request.getTaken() != null) {
            if (request.getTaken()) {
                log.markAsTaken();
            } else {
                log.markAsNotTaken();
            }
        }

        if (request.getNotes() != null) {
            log.updateNotes(request.getNotes());
        }

        return prescriptionHistoryStore.saveMedicationLog(log);
    }

    /**
     * 처방 이력 기록 (처방 생성/변경 시 자동 호출)
     */
    @Transactional
    public PrescriptionHistory recordPrescriptionHistory(Prescription prescription, String actionType, String changedBy) {
        PrescriptionHistory history = PrescriptionHistory.create(prescription, actionType, changedBy);
        return prescriptionHistoryStore.savePrescriptionHistory(history);
    }

    /**
     * 처방 변경 이력 기록
     */
    @Transactional
    public PrescriptionHistory recordPrescriptionChange(Prescription prescription, String changedField,
                                                       String oldValue, String newValue, String changedBy, String reason) {
        PrescriptionHistory history = PrescriptionHistory.createChange(
                prescription, changedField, oldValue, newValue, changedBy, reason);
        return prescriptionHistoryStore.savePrescriptionHistory(history);
    }
}

