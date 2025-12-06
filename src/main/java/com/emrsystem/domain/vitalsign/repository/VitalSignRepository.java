package com.emrsystem.domain.vitalsign.repository;

import com.emrsystem.domain.vitalsign.entity.VitalSign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {
    List<VitalSign> findByPatient_PatientId(Long patientId);
    Page<VitalSign> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<VitalSign> findByPatient_PatientIdAndMeasuredAtBetween(Long patientId, LocalDateTime start, LocalDateTime end);
    List<VitalSign> findByPatient_PatientIdOrderByMeasuredAtDesc(Long patientId);
}

