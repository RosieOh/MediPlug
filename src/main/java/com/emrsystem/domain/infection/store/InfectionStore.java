package com.emrsystem.domain.infection.store;

import com.emrsystem.domain.infection.entity.InfectionCase;
import com.emrsystem.domain.infection.entity.InfectionPrevention;
import com.emrsystem.domain.infection.repository.InfectionCaseRepository;
import com.emrsystem.domain.infection.repository.InfectionPreventionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InfectionStore {

    private final InfectionCaseRepository infectionCaseRepository;
    private final InfectionPreventionRepository infectionPreventionRepository;

    // InfectionCase operations
    public InfectionCase saveInfectionCase(InfectionCase infectionCase) {
        return infectionCaseRepository.save(infectionCase);
    }

    public Optional<InfectionCase> findInfectionCaseById(Long id) {
        return infectionCaseRepository.findById(id);
    }

    public List<InfectionCase> findInfectionCasesByPatient(Long patientId) {
        return infectionCaseRepository.findByPatient_PatientId(patientId);
    }

    public Page<InfectionCase> findInfectionCasesByPatient(Long patientId, Pageable pageable) {
        return infectionCaseRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<InfectionCase> findInfectionCasesByStatus(String status) {
        return infectionCaseRepository.findByStatus(status);
    }

    public List<InfectionCase> findInfectionCasesByType(String infectionType) {
        return infectionCaseRepository.findByInfectionType(infectionType);
    }

    public List<InfectionCase> findInfectionCasesByDateRange(LocalDate start, LocalDate end) {
        return infectionCaseRepository.findByDiagnosedDateBetween(start, end);
    }

    public List<InfectionCase> findInfectionCasesByIsolationRoomAndStatus(String isolationRoom, String status) {
        return infectionCaseRepository.findByIsolationRoomAndStatus(isolationRoom, status);
    }

    // InfectionPrevention operations
    public InfectionPrevention saveInfectionPrevention(InfectionPrevention prevention) {
        return infectionPreventionRepository.save(prevention);
    }

    public List<InfectionPrevention> findInfectionPreventionsByCase(Long infectionCaseId) {
        return infectionPreventionRepository.findByInfectionCase_InfectionCaseId(infectionCaseId);
    }

    public List<InfectionPrevention> findActiveInfectionPreventionsByCase(Long infectionCaseId) {
        return infectionPreventionRepository.findByInfectionCase_InfectionCaseIdAndActiveTrue(infectionCaseId);
    }

    public Optional<InfectionPrevention> findInfectionPreventionById(Long id) {
        return infectionPreventionRepository.findById(id);
    }
}

