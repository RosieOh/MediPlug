package com.emrsystem.domain.patient.repository;

import com.emrsystem.domain.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Patient> findByIdentifierContainingIgnoreCase(String identifier, Pageable pageable);
    Page<Patient> findByNameContainingIgnoreCaseAndIdentifierContainingIgnoreCase(String name, String identifier, Pageable pageable);
}


