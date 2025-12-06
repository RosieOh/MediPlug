package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.MedicalImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalImageRepository extends JpaRepository<MedicalImage, Long> {
    List<MedicalImage> findByPatient_PatientId(Long patientId);
    Page<MedicalImage> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<MedicalImage> findByMedicalRecord_MedicalRecordId(Long medicalRecordId);
    List<MedicalImage> findByLabOrder_LabOrderId(Long labOrderId);
    List<MedicalImage> findByImageTypeAndStatus(String imageType, String status);
    List<MedicalImage> findByStatus(String status);
}

