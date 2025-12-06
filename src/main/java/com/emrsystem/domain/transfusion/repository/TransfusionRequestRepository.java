package com.emrsystem.domain.transfusion.repository;

import com.emrsystem.domain.transfusion.entity.TransfusionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransfusionRequestRepository extends JpaRepository<TransfusionRequest, Long> {
    List<TransfusionRequest> findByPatient_PatientId(Long patientId);
    Page<TransfusionRequest> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<TransfusionRequest> findByStatus(String status);
    List<TransfusionRequest> findByRequestingDoctor_DoctorId(Long doctorId);
}

