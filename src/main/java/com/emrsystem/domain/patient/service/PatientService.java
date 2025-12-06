package com.emrsystem.domain.patient.service;

import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.request.PatientRequests.CreatePatientRequest;
import com.emrsystem.domain.patient.request.PatientRequests.UpdatePatientRequest;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientService {

    private final PatientStore patientStore;

    public Page<Patient> page(Pageable pageable) { return patientStore.page(pageable); }

    public Page<Patient> search(String name, String identifier, Pageable pageable) {
        return patientStore.search(name, identifier, pageable);
    }

    public Patient get(Long id) {
        return patientStore.findById(id).orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));
    }

    @Transactional
    public Patient create(CreatePatientRequest request) {
        Patient patient = Patient.create(
                request.getName(),
                request.getBirthDate(),
                request.getGender(),
                request.getPhone(),
                request.getAddress(),
                request.getIdentifier()
        );
        return patientStore.save(patient);
    }

    @Transactional
    public Patient update(Long id, UpdatePatientRequest request) {
        Patient patient = get(id);
        patient.update(
                request.getName(),
                request.getBirthDate(),
                request.getGender(),
                request.getPhone(),
                request.getAddress()
        );
        return patientStore.save(patient);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        patientStore.deleteById(id);
    }
}


