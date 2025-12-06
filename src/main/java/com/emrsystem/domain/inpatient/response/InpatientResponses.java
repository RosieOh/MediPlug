package com.emrsystem.domain.inpatient.response;

import com.emrsystem.domain.inpatient.entity.InpatientDailyRecord;
import com.emrsystem.domain.inpatient.entity.NursingNote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InpatientResponses {

    @Getter
    @NoArgsConstructor
    public static class InpatientDailyRecordSummary {
        private Long dailyRecordId;
        private Long admissionId;
        private Long patientId;
        private String patientName;
        private LocalDate recordDate;
        private Long doctorId;
        private String doctorName;
        private String condition;
        private String treatment;
        private String plan;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static InpatientDailyRecordSummary of(InpatientDailyRecord record) {
            InpatientDailyRecordSummary summary = new InpatientDailyRecordSummary();
            summary.dailyRecordId = record.getId();
            summary.admissionId = record.getAdmission().getId();
            summary.patientId = record.getAdmission().getPatient().getId();
            summary.patientName = record.getAdmission().getPatient().getName();
            summary.recordDate = record.getRecordDate();
            summary.doctorId = record.getDoctor() != null ? record.getDoctor().getId() : null;
            summary.doctorName = record.getDoctor() != null ? record.getDoctor().getName() : null;
            summary.condition = record.getCondition();
            summary.treatment = record.getTreatment();
            summary.plan = record.getPlan();
            summary.notes = record.getNotes();
            summary.createdAt = record.getCreatedAt();
            summary.updatedAt = record.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NursingNoteSummary {
        private Long nursingNoteId;
        private Long admissionId;
        private Long patientId;
        private String patientName;
        private LocalDateTime recordedAt;
        private String recordedBy;
        private String noteType;
        private String content;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static NursingNoteSummary of(NursingNote note) {
            return new NursingNoteSummary(
                    note.getId(),
                    note.getAdmission().getId(),
                    note.getAdmission().getPatient().getId(),
                    note.getAdmission().getPatient().getName(),
                    note.getRecordedAt(),
                    note.getRecordedBy(),
                    note.getNoteType(),
                    note.getContent(),
                    note.getNotes(),
                    note.getCreatedAt(),
                    note.getUpdatedAt()
            );
        }
    }
}

