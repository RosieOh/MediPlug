package com.emrsystem.domain.inpatient.facade;

import com.emrsystem.domain.inpatient.entity.InpatientDailyRecord;
import com.emrsystem.domain.inpatient.entity.NursingNote;
import com.emrsystem.domain.inpatient.request.InpatientRequests;
import com.emrsystem.domain.inpatient.service.InpatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InpatientFacade {

    private final InpatientService inpatientService;

    public InpatientDailyRecord getDailyRecord(Long id) {
        return inpatientService.getDailyRecord(id);
    }

    public List<InpatientDailyRecord> getDailyRecordsByAdmission(Long admissionId) {
        return inpatientService.getDailyRecordsByAdmission(admissionId);
    }

    public List<InpatientDailyRecord> getDailyRecordsByAdmissionAndDate(Long admissionId, LocalDate recordDate) {
        return inpatientService.getDailyRecordsByAdmissionAndDate(admissionId, recordDate);
    }

    public InpatientDailyRecord createDailyRecord(InpatientRequests.CreateDailyRecordRequest request) {
        return inpatientService.createDailyRecord(request);
    }

    public InpatientDailyRecord updateDailyRecord(Long id, InpatientRequests.UpdateDailyRecordRequest request) {
        return inpatientService.updateDailyRecord(id, request);
    }

    public NursingNote getNursingNote(Long id) {
        return inpatientService.getNursingNote(id);
    }

    public List<NursingNote> getNursingNotesByAdmission(Long admissionId) {
        return inpatientService.getNursingNotesByAdmission(admissionId);
    }

    public Page<NursingNote> getNursingNotesByAdmission(Long admissionId, Pageable pageable) {
        return inpatientService.getNursingNotesByAdmission(admissionId, pageable);
    }

    public List<NursingNote> getNursingNotesByAdmissionAndDateRange(Long admissionId, LocalDateTime start, LocalDateTime end) {
        return inpatientService.getNursingNotesByAdmissionAndDateRange(admissionId, start, end);
    }

    public List<NursingNote> getNursingNotesByAdmissionAndType(Long admissionId, String noteType) {
        return inpatientService.getNursingNotesByAdmissionAndType(admissionId, noteType);
    }

    public NursingNote createNursingNote(InpatientRequests.CreateNursingNoteRequest request) {
        return inpatientService.createNursingNote(request);
    }

    public NursingNote updateNursingNote(Long id, InpatientRequests.UpdateNursingNoteRequest request) {
        return inpatientService.updateNursingNote(id, request);
    }
}

