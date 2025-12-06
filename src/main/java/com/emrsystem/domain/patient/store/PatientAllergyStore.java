package com.emrsystem.domain.patient.store;

import com.emrsystem.domain.patient.entity.PatientAllergy;
import com.emrsystem.domain.patient.repository.PatientAllergyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PatientAllergyStore {

    private final PatientAllergyRepository patientAllergyRepository;

    public PatientAllergy save(PatientAllergy allergy) {
        return patientAllergyRepository.save(allergy);
    }

    public Optional<PatientAllergy> findById(Long id) {
        return patientAllergyRepository.findById(id);
    }

    public List<PatientAllergy> findByPatientId(Long patientId) {
        return patientAllergyRepository.findByPatient_PatientIdAndActiveTrue(patientId);
    }

    public List<PatientAllergy> findAllByPatientId(Long patientId) {
        return patientAllergyRepository.findByPatient_PatientId(patientId);
    }

    public List<PatientAllergy> findByAllergenType(String allergenType) {
        return patientAllergyRepository.findByAllergenTypeAndActiveTrue(allergenType);
    }

    public boolean existsByPatientIdAndAllergenName(Long patientId, String allergenName) {
        return patientAllergyRepository.existsByPatient_PatientIdAndAllergenNameIgnoreCaseAndActiveTrue(patientId, allergenName);
    }

    public void deleteById(Long id) {
        patientAllergyRepository.deleteById(id);
    }
}

