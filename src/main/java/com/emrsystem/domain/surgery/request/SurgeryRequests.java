package com.emrsystem.domain.surgery.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SurgeryRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateSurgeryRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        private Long appointmentId;

        @NotBlank(message = "수술명은 필수입니다.")
        @Size(max = 200, message = "수술명은 200자를 초과할 수 없습니다.")
        private String surgeryName;

        @Size(max = 50, message = "수술 코드는 50자를 초과할 수 없습니다.")
        private String surgeryCode;

        @NotNull(message = "예정 일시는 필수입니다.")
        private LocalDateTime scheduledDateTime;

        @NotNull(message = "주의사 ID는 필수입니다.")
        private Long surgeonId;

        private Long anesthesiologistId;

        @Size(max = 50, message = "마취 방법은 50자를 초과할 수 없습니다.")
        private String anesthesiaType;

        @Size(max = 50, message = "수술실은 50자를 초과할 수 없습니다.")
        private String surgeryRoom;

        private String preOpDiagnosis;

        private List<TeamMemberRequest> teamMembers; // 수술팀 구성원

        private List<String> checklistItems; // 체크리스트 항목
    }

    @Getter
    @NoArgsConstructor
    public static class TeamMemberRequest {
        @NotNull(message = "의사 ID는 필수입니다.")
        private Long doctorId;

        @NotBlank(message = "역할은 필수입니다.")
        @Size(max = 50, message = "역할은 50자를 초과할 수 없습니다.")
        private String role; // 주의사, 제1조수, 제2조수, 마취의 등

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateSurgeryRequest {
        @Size(max = 200, message = "수술명은 200자를 초과할 수 없습니다.")
        private String surgeryName;

        @Size(max = 50, message = "수술 코드는 50자를 초과할 수 없습니다.")
        private String surgeryCode;

        private LocalDateTime scheduledDateTime;

        private Long anesthesiologistId;

        @Size(max = 50, message = "마취 방법은 50자를 초과할 수 없습니다.")
        private String anesthesiaType;

        @Size(max = 50, message = "수술실은 50자를 초과할 수 없습니다.")
        private String surgeryRoom;

        private String preOpDiagnosis;
    }

    @Getter
    @NoArgsConstructor
    public static class CompleteSurgeryRequest {
        @NotNull(message = "종료 일시는 필수입니다.")
        private LocalDateTime endDateTime;

        private String postOpDiagnosis;

        private String procedure;

        private String findings;
    }

    @Getter
    @NoArgsConstructor
    public static class AddComplicationRequest {
        @NotBlank(message = "합병증 내용은 필수입니다.")
        private String complication;
    }
}

