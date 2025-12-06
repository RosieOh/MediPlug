package com.emrsystem.domain.surgery.response;

import com.emrsystem.domain.surgery.entity.PreOpChecklist;
import com.emrsystem.domain.surgery.entity.Surgery;
import com.emrsystem.domain.surgery.entity.SurgeryTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SurgeryResponses {

    @Getter
    @NoArgsConstructor
    public static class SurgerySummary {
        private Long surgeryId;
        private Long patientId;
        private String patientName;
        private Long appointmentId;
        private String surgeryName;
        private String surgeryCode;
        private LocalDateTime scheduledDateTime;
        private LocalDateTime actualStartDateTime;
        private LocalDateTime actualEndDateTime;
        private Long surgeonId;
        private String surgeonName;
        private Long anesthesiologistId;
        private String anesthesiologistName;
        private String anesthesiaType;
        private String surgeryRoom;
        private String status;
        private String preOpDiagnosis;
        private String postOpDiagnosis;
        private String procedure;
        private String findings;
        private String complications;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static SurgerySummary of(Surgery surgery) {
            SurgerySummary summary = new SurgerySummary();
            summary.surgeryId = surgery.getId();
            summary.patientId = surgery.getPatient().getId();
            summary.patientName = surgery.getPatient().getName();
            summary.appointmentId = surgery.getAppointment() != null ? surgery.getAppointment().getId() : null;
            summary.surgeryName = surgery.getSurgeryName();
            summary.surgeryCode = surgery.getSurgeryCode();
            summary.scheduledDateTime = surgery.getScheduledDateTime();
            summary.actualStartDateTime = surgery.getActualStartDateTime();
            summary.actualEndDateTime = surgery.getActualEndDateTime();
            summary.surgeonId = surgery.getSurgeon().getId();
            summary.surgeonName = surgery.getSurgeon().getName();
            summary.anesthesiologistId = surgery.getAnesthesiologist() != null ? surgery.getAnesthesiologist().getId() : null;
            summary.anesthesiologistName = surgery.getAnesthesiologist() != null ? surgery.getAnesthesiologist().getName() : null;
            summary.anesthesiaType = surgery.getAnesthesiaType();
            summary.surgeryRoom = surgery.getSurgeryRoom();
            summary.status = surgery.getStatus();
            summary.preOpDiagnosis = surgery.getPreOpDiagnosis();
            summary.postOpDiagnosis = surgery.getPostOpDiagnosis();
            summary.procedure = surgery.getProcedure();
            summary.findings = surgery.getFindings();
            summary.complications = surgery.getComplications();
            summary.notes = surgery.getNotes();
            summary.createdAt = surgery.getCreatedAt();
            summary.updatedAt = surgery.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SurgeryTeamSummary {
        private Long surgeryTeamId;
        private Long surgeryId;
        private Long doctorId;
        private String doctorName;
        private String role;
        private String notes;

        public static SurgeryTeamSummary of(SurgeryTeam team) {
            return new SurgeryTeamSummary(
                    team.getId(),
                    team.getSurgery().getId(),
                    team.getDoctor().getId(),
                    team.getDoctor().getName(),
                    team.getRole(),
                    team.getNotes()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PreOpChecklistSummary {
        private Long checklistId;
        private Long surgeryId;
        private String item;
        private boolean checked;
        private String checkedBy;
        private LocalDateTime checkedAt;
        private String notes;

        public static PreOpChecklistSummary of(PreOpChecklist checklist) {
            return new PreOpChecklistSummary(
                    checklist.getId(),
                    checklist.getSurgery().getId(),
                    checklist.getItem(),
                    checklist.isChecked(),
                    checklist.getCheckedBy(),
                    checklist.getCheckedAt(),
                    checklist.getNotes()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SurgeryDetail {
        private SurgerySummary surgery;
        private List<SurgeryTeamSummary> teamMembers;
        private List<PreOpChecklistSummary> checklist;

        public static SurgeryDetail of(Surgery surgery, List<SurgeryTeam> teamMembers, List<PreOpChecklist> checklist) {
            SurgeryDetail detail = new SurgeryDetail();
            detail.surgery = SurgerySummary.of(surgery);
            detail.teamMembers = teamMembers.stream()
                    .map(SurgeryTeamSummary::of)
                    .collect(Collectors.toList());
            detail.checklist = checklist.stream()
                    .map(PreOpChecklistSummary::of)
                    .collect(Collectors.toList());
            return detail;
        }
    }
}

