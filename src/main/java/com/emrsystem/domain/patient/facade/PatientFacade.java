package com.emrsystem.domain.patient.facade;

import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.request.PatientRequests.CreatePatientRequest;
import com.emrsystem.domain.patient.request.PatientRequests.UpdatePatientRequest;
import com.emrsystem.domain.patient.response.PatientResponses.PatientSummary;
import com.emrsystem.domain.patient.service.PatientService;
import com.emrsystem.global.common.dto.PageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientFacade {

    private final PatientService patientService;

    public PageResponse<PatientSummary> page(Pageable pageable) {
        Page<com.emrsystem.domain.patient.entity.Patient> page = patientService.page(pageable);
        return PageResponse.of(page.map(PatientSummary::from));
    }

    public PageResponse<PatientSummary> search(String name, String identifier, Pageable pageable) {
        Page<com.emrsystem.domain.patient.entity.Patient> page = patientService.search(name, identifier, pageable);
        return PageResponse.of(page.map(PatientSummary::from));
    }

    public PatientSummary get(Long id) {
        Patient patient = patientService.get(id);
        return PatientSummary.from(patient);
    }

    public PatientSummary create(CreatePatientRequest request) {
        Patient patient = patientService.create(request);
        return PatientSummary.from(patient);
    }

    public PatientSummary update(Long id, UpdatePatientRequest request) {
        Patient patient = patientService.update(id, request);
        return PatientSummary.from(patient);
    }

    public void delete(Long id) {
        patientService.delete(id);
    }
}


