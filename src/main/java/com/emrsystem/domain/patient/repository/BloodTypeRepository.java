package com.emrsystem.domain.patient.repository;

import com.emrsystem.domain.patient.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {
    Optional<BloodType> findByPatient_PatientId(Long patientId);
}

