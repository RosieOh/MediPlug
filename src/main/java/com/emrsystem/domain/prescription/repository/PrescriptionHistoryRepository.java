package com.emrsystem.domain.prescription.repository;

import com.emrsystem.domain.prescription.entity.PrescriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionHistoryRepository extends JpaRepository<PrescriptionHistory, Long> {
    List<PrescriptionHistory> findByPrescription_PrescriptionId(Long prescriptionId);
    List<PrescriptionHistory> findByPrescription_PrescriptionIdOrderByCreatedAtDesc(Long prescriptionId);
    List<PrescriptionHistory> findByActionType(String actionType);
}

