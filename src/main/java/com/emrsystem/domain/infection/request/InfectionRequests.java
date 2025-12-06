package com.emrsystem.domain.infection.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class InfectionRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateInfectionCaseRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        @NotBlank(message = "감염 유형은 필수입니다.")
        @Size(max = 100, message = "감염 유형은 100자를 초과할 수 없습니다.")
        private String infectionType; // MRSA, VRE, C.diff, COVID-19 등

        @NotBlank(message = "감염명은 필수입니다.")
        @Size(max = 200, message = "감염명은 200자를 초과할 수 없습니다.")
        private String infectionName;

        @NotNull(message = "진단일은 필수입니다.")
        private LocalDate diagnosedDate;

        @NotBlank(message = "심각도는 필수입니다.")
        @Size(max = 50, message = "심각도는 50자를 초과할 수 없습니다.")
        private String severity; // MILD, MODERATE, SEVERE, CRITICAL

        @Size(max = 100, message = "격리실은 100자를 초과할 수 없습니다.")
        private String isolationRoom;

        private String symptoms;

        private String treatment;

        private String preventionMeasures;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateInfectionCaseRequest {
        @Size(max = 100, message = "감염 유형은 100자를 초과할 수 없습니다.")
        private String infectionType;

        @Size(max = 200, message = "감염명은 200자를 초과할 수 없습니다.")
        private String infectionName;

        @Size(max = 50, message = "심각도는 50자를 초과할 수 없습니다.")
        private String severity;

        @Size(max = 100, message = "격리실은 100자를 초과할 수 없습니다.")
        private String isolationRoom;

        private String symptoms;

        private String treatment;

        private String preventionMeasures;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreatePreventionRequest {
        @NotNull(message = "감염 사례 ID는 필수입니다.")
        private Long infectionCaseId;

        @NotBlank(message = "예방 조치는 필수입니다.")
        @Size(max = 200, message = "예방 조치는 200자를 초과할 수 없습니다.")
        private String measure;

        @NotBlank(message = "시행한 사람은 필수입니다.")
        @Size(max = 200, message = "시행한 사람은 200자를 초과할 수 없습니다.")
        private String implementedBy;
    }
}

