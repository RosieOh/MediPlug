package com.emrsystem.domain.inpatient.repository;

import com.emrsystem.domain.inpatient.entity.NursingNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NursingNoteRepository extends JpaRepository<NursingNote, Long> {
    List<NursingNote> findByAdmission_AdmissionId(Long admissionId);
    Page<NursingNote> findByAdmission_AdmissionId(Long admissionId, Pageable pageable);
    List<NursingNote> findByAdmission_AdmissionIdAndRecordedAtBetween(Long admissionId, LocalDateTime start, LocalDateTime end);
    List<NursingNote> findByAdmission_AdmissionIdAndNoteType(Long admissionId, String noteType);
}

