package com.emrsystem.domain.inpatient.store;

import com.emrsystem.domain.inpatient.entity.InpatientDailyRecord;
import com.emrsystem.domain.inpatient.entity.NursingNote;
import com.emrsystem.domain.inpatient.repository.InpatientDailyRecordRepository;
import com.emrsystem.domain.inpatient.repository.NursingNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InpatientStore {

    private final InpatientDailyRecordRepository inpatientDailyRecordRepository;
    private final NursingNoteRepository nursingNoteRepository;

    // InpatientDailyRecord operations
    public InpatientDailyRecord saveDailyRecord(InpatientDailyRecord record) {
        return inpatientDailyRecordRepository.save(record);
    }

    public Optional<InpatientDailyRecord> findDailyRecordById(Long id) {
        return inpatientDailyRecordRepository.findById(id);
    }

    public List<InpatientDailyRecord> findDailyRecordsByAdmission(Long admissionId) {
        return inpatientDailyRecordRepository.findByAdmission_AdmissionId(admissionId);
    }

    public List<InpatientDailyRecord> findDailyRecordsByAdmissionAndDate(Long admissionId, LocalDate recordDate) {
        return inpatientDailyRecordRepository.findByAdmission_AdmissionIdAndRecordDate(admissionId, recordDate);
    }

    public List<InpatientDailyRecord> findDailyRecordsByPatient(Long patientId) {
        return inpatientDailyRecordRepository.findByAdmission_Patient_PatientId(patientId);
    }

    // NursingNote operations
    public NursingNote saveNursingNote(NursingNote note) {
        return nursingNoteRepository.save(note);
    }

    public Optional<NursingNote> findNursingNoteById(Long id) {
        return nursingNoteRepository.findById(id);
    }

    public List<NursingNote> findNursingNotesByAdmission(Long admissionId) {
        return nursingNoteRepository.findByAdmission_AdmissionId(admissionId);
    }

    public Page<NursingNote> findNursingNotesByAdmission(Long admissionId, Pageable pageable) {
        return nursingNoteRepository.findByAdmission_AdmissionId(admissionId, pageable);
    }

    public List<NursingNote> findNursingNotesByAdmissionAndDateRange(Long admissionId, LocalDateTime start, LocalDateTime end) {
        return nursingNoteRepository.findByAdmission_AdmissionIdAndRecordedAtBetween(admissionId, start, end);
    }

    public List<NursingNote> findNursingNotesByAdmissionAndType(Long admissionId, String noteType) {
        return nursingNoteRepository.findByAdmission_AdmissionIdAndNoteType(admissionId, noteType);
    }
}

