package com.emrsystem.domain.transfusion.repository;

import com.emrsystem.domain.transfusion.entity.TransfusionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransfusionRecordRepository extends JpaRepository<TransfusionRecord, Long> {
    List<TransfusionRecord> findByPatient_PatientId(Long patientId);
    List<TransfusionRecord> findByTransfusionRequest_TransfusionRequestId(Long transfusionRequestId);
    List<TransfusionRecord> findByStatus(String status);
}

