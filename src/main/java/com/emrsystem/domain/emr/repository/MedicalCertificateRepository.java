package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.MedicalCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalCertificateRepository extends JpaRepository<MedicalCertificate, Long> {
    Optional<MedicalCertificate> findByCertificateNumber(String certificateNumber);
    List<MedicalCertificate> findByPatient_PatientId(Long patientId);
    Page<MedicalCertificate> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<MedicalCertificate> findByCertificateTypeAndStatus(String certificateType, String status);
    List<MedicalCertificate> findByStatus(String status);
    List<MedicalCertificate> findByExpiryDateBeforeAndStatus(LocalDate date, String status);
}

