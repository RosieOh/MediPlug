package com.emrsystem.domain.patient.repository;

import com.emrsystem.domain.patient.entity.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatient_PatientIdAndActiveTrue(Long patientId);
    List<PatientAllergy> findByPatient_PatientId(Long patientId);
    List<PatientAllergy> findByAllergenTypeAndActiveTrue(String allergenType);
    boolean existsByPatient_PatientIdAndAllergenNameIgnoreCaseAndActiveTrue(Long patientId, String allergenName);
}

