package com.emrsystem.domain.inpatient.repository;

import com.emrsystem.domain.inpatient.entity.InpatientDailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InpatientDailyRecordRepository extends JpaRepository<InpatientDailyRecord, Long> {
    List<InpatientDailyRecord> findByAdmission_AdmissionId(Long admissionId);
    List<InpatientDailyRecord> findByAdmission_AdmissionIdAndRecordDate(Long admissionId, LocalDate recordDate);
    List<InpatientDailyRecord> findByAdmission_Patient_PatientId(Long patientId);
}

