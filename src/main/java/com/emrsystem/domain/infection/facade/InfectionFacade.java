package com.emrsystem.domain.infection.facade;

import com.emrsystem.domain.infection.entity.InfectionCase;
import com.emrsystem.domain.infection.entity.InfectionPrevention;
import com.emrsystem.domain.infection.request.InfectionRequests;
import com.emrsystem.domain.infection.service.InfectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InfectionFacade {

    private final InfectionService infectionService;

    public InfectionCase get(Long id) {
        return infectionService.get(id);
    }

    public List<InfectionCase> getByPatient(Long patientId) {
        return infectionService.getByPatient(patientId);
    }

    public Page<InfectionCase> getByPatient(Long patientId, Pageable pageable) {
        return infectionService.getByPatient(patientId, pageable);
    }

    public List<InfectionCase> getByStatus(String status) {
        return infectionService.getByStatus(status);
    }

    public List<InfectionCase> getByType(String infectionType) {
        return infectionService.getByType(infectionType);
    }

    public List<InfectionCase> getByDateRange(LocalDate start, LocalDate end) {
        return infectionService.getByDateRange(start, end);
    }

    public List<InfectionCase> getByIsolationRoom(String isolationRoom) {
        return infectionService.getByIsolationRoom(isolationRoom);
    }

    public InfectionCase create(InfectionRequests.CreateInfectionCaseRequest request) {
        return infectionService.create(request);
    }

    public InfectionCase update(Long id, InfectionRequests.UpdateInfectionCaseRequest request) {
        return infectionService.update(id, request);
    }

    public InfectionCase resolve(Long id, LocalDate resolvedDate) {
        return infectionService.resolve(id, resolvedDate);
    }

    public InfectionCase setMonitoring(Long id) {
        return infectionService.setMonitoring(id);
    }

    public List<InfectionPrevention> getPreventions(Long infectionCaseId) {
        return infectionService.getPreventions(infectionCaseId);
    }

    public InfectionPrevention createPrevention(InfectionRequests.CreatePreventionRequest request) {
        return infectionService.createPrevention(request);
    }

    public InfectionPrevention deactivatePrevention(Long preventionId) {
        return infectionService.deactivatePrevention(preventionId);
    }
}

