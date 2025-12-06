package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.LabOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    Page<LabOrder> findByMedicalRecord_MedicalRecordId(Long medicalRecordId, Pageable pageable);
    Page<LabOrder> findByStatus(String status, Pageable pageable);
}