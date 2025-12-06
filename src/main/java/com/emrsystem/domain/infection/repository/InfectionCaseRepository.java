package com.emrsystem.domain.infection.repository;

import com.emrsystem.domain.infection.entity.InfectionCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InfectionCaseRepository extends JpaRepository<InfectionCase, Long> {
    List<InfectionCase> findByPatient_PatientId(Long patientId);
    Page<InfectionCase> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<InfectionCase> findByStatus(String status);
    List<InfectionCase> findByInfectionType(String infectionType);
    List<InfectionCase> findByDiagnosedDateBetween(LocalDate start, LocalDate end);
    List<InfectionCase> findByIsolationRoomAndStatus(String isolationRoom, String status);
}

