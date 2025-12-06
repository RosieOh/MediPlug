package com.emrsystem.domain.inpatient.service;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.store.BedStore;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.inpatient.entity.InpatientDailyRecord;
import com.emrsystem.domain.inpatient.entity.NursingNote;
import com.emrsystem.domain.inpatient.request.InpatientRequests;
import com.emrsystem.domain.inpatient.store.InpatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InpatientService {

    private final InpatientStore inpatientStore;
    private final BedStore bedStore;
    private final DoctorStore doctorStore;

    public InpatientDailyRecord getDailyRecord(Long id) {
        return inpatientStore.findDailyRecordById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "일일 기록을 찾을 수 없습니다."));
    }

    public List<InpatientDailyRecord> getDailyRecordsByAdmission(Long admissionId) {
        return inpatientStore.findDailyRecordsByAdmission(admissionId);
    }

    public List<InpatientDailyRecord> getDailyRecordsByAdmissionAndDate(Long admissionId, LocalDate recordDate) {
        return inpatientStore.findDailyRecordsByAdmissionAndDate(admissionId, recordDate);
    }

    @Transactional
    public InpatientDailyRecord createDailyRecord(InpatientRequests.CreateDailyRecordRequest request) {
        Admission admission = bedStore.findAdmissionById(request.getAdmissionId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "입원 정보를 찾을 수 없습니다."));

        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorStore.findById(request.getDoctorId())
                    .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));
        }

        InpatientDailyRecord record = InpatientDailyRecord.create(
                admission,
                request.getRecordDate() != null ? request.getRecordDate() : LocalDate.now(),
                doctor,
                request.getCondition(),
                request.getTreatment(),
                request.getPlan(),
                request.getNotes()
        );

        return inpatientStore.saveDailyRecord(record);
    }

    @Transactional
    public InpatientDailyRecord updateDailyRecord(Long id, InpatientRequests.UpdateDailyRecordRequest request) {
        InpatientDailyRecord record = getDailyRecord(id);
        record.update(request.getCondition(), request.getTreatment(), request.getPlan(), request.getNotes());
        return inpatientStore.saveDailyRecord(record);
    }

    // NursingNote operations
    public NursingNote getNursingNote(Long id) {
        return inpatientStore.findNursingNoteById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "간병 기록을 찾을 수 없습니다."));
    }

    public List<NursingNote> getNursingNotesByAdmission(Long admissionId) {
        return inpatientStore.findNursingNotesByAdmission(admissionId);
    }

    public Page<NursingNote> getNursingNotesByAdmission(Long admissionId, Pageable pageable) {
        return inpatientStore.findNursingNotesByAdmission(admissionId, pageable);
    }

    public List<NursingNote> getNursingNotesByAdmissionAndDateRange(Long admissionId, LocalDateTime start, LocalDateTime end) {
        return inpatientStore.findNursingNotesByAdmissionAndDateRange(admissionId, start, end);
    }

    public List<NursingNote> getNursingNotesByAdmissionAndType(Long admissionId, String noteType) {
        return inpatientStore.findNursingNotesByAdmissionAndType(admissionId, noteType);
    }

    @Transactional
    public NursingNote createNursingNote(InpatientRequests.CreateNursingNoteRequest request) {
        Admission admission = bedStore.findAdmissionById(request.getAdmissionId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "입원 정보를 찾을 수 없습니다."));

        NursingNote note = NursingNote.create(
                admission,
                request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now(),
                request.getRecordedBy(),
                request.getNoteType(),
                request.getContent(),
                request.getNotes()
        );

        return inpatientStore.saveNursingNote(note);
    }

    @Transactional
    public NursingNote updateNursingNote(Long id, InpatientRequests.UpdateNursingNoteRequest request) {
        NursingNote note = getNursingNote(id);
        note.update(request.getNoteType(), request.getContent(), request.getNotes());
        return inpatientStore.saveNursingNote(note);
    }
}

