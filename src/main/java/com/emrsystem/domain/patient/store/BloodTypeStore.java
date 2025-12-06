package com.emrsystem.domain.patient.store;

import com.emrsystem.domain.patient.entity.BloodType;
import com.emrsystem.domain.patient.repository.BloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BloodTypeStore {

    private final BloodTypeRepository bloodTypeRepository;

    public BloodType save(BloodType bloodType) {
        return bloodTypeRepository.save(bloodType);
    }

    public Optional<BloodType> findByPatientId(Long patientId) {
        return bloodTypeRepository.findByPatient_PatientId(patientId);
    }
}

