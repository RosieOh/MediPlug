package com.emrsystem.domain.infection.service;

import com.emrsystem.domain.infection.entity.InfectionCase;
import com.emrsystem.domain.infection.entity.InfectionPrevention;
import com.emrsystem.domain.infection.request.InfectionRequests;
import com.emrsystem.domain.infection.store.InfectionStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfectionService {

    private final InfectionStore infectionStore;
    private final PatientStore patientStore;

    public InfectionCase get(Long id) {
        return infectionStore.findInfectionCaseById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "감염 사례를 찾을 수 없습니다."));
    }

    public List<InfectionCase> getByPatient(Long patientId) {
        return infectionStore.findInfectionCasesByPatient(patientId);
    }

    public Page<InfectionCase> getByPatient(Long patientId, Pageable pageable) {
        return infectionStore.findInfectionCasesByPatient(patientId, pageable);
    }

    public List<InfectionCase> getByStatus(String status) {
        return infectionStore.findInfectionCasesByStatus(status);
    }

    public List<InfectionCase> getByType(String infectionType) {
        return infectionStore.findInfectionCasesByType(infectionType);
    }

    public List<InfectionCase> getByDateRange(LocalDate start, LocalDate end) {
        return infectionStore.findInfectionCasesByDateRange(start, end);
    }

    public List<InfectionCase> getByIsolationRoom(String isolationRoom) {
        return infectionStore.findInfectionCasesByIsolationRoomAndStatus(isolationRoom, "ACTIVE");
    }

    @Transactional
    public InfectionCase create(InfectionRequests.CreateInfectionCaseRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        InfectionCase infectionCase = InfectionCase.create(
                patient,
                request.getInfectionType(),
                request.getInfectionName(),
                request.getDiagnosedDate(),
                request.getSeverity(),
                request.getIsolationRoom(),
                request.getSymptoms(),
                request.getTreatment(),
                request.getPreventionMeasures()
        );

        return infectionStore.saveInfectionCase(infectionCase);
    }

    @Transactional
    public InfectionCase update(Long id, InfectionRequests.UpdateInfectionCaseRequest request) {
        InfectionCase infectionCase = get(id);
        infectionCase.update(
                request.getInfectionType(),
                request.getInfectionName(),
                request.getSeverity(),
                request.getIsolationRoom(),
                request.getSymptoms(),
                request.getTreatment(),
                request.getPreventionMeasures(),
                request.getNotes()
        );
        return infectionStore.saveInfectionCase(infectionCase);
    }

    @Transactional
    public InfectionCase resolve(Long id, LocalDate resolvedDate) {
        InfectionCase infectionCase = get(id);
        infectionCase.resolve(resolvedDate);
        return infectionStore.saveInfectionCase(infectionCase);
    }

    @Transactional
    public InfectionCase setMonitoring(Long id) {
        InfectionCase infectionCase = get(id);
        infectionCase.setMonitoring();
        return infectionStore.saveInfectionCase(infectionCase);
    }

    // InfectionPrevention operations
    public List<InfectionPrevention> getPreventions(Long infectionCaseId) {
        return infectionStore.findActiveInfectionPreventionsByCase(infectionCaseId);
    }

    @Transactional
    public InfectionPrevention createPrevention(InfectionRequests.CreatePreventionRequest request) {
        InfectionCase infectionCase = get(request.getInfectionCaseId());
        InfectionPrevention prevention = InfectionPrevention.create(
                infectionCase,
                request.getMeasure(),
                request.getImplementedBy()
        );
        return infectionStore.saveInfectionPrevention(prevention);
    }

    @Transactional
    public InfectionPrevention deactivatePrevention(Long preventionId) {
        InfectionPrevention prevention = infectionStore.findInfectionPreventionById(preventionId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "예방 조치를 찾을 수 없습니다."));
        prevention.deactivate();
        return infectionStore.saveInfectionPrevention(prevention);
    }
}

