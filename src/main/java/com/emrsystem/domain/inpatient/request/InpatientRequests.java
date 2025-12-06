package com.emrsystem.domain.inpatient.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InpatientRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateDailyRecordRequest {
        @NotNull(message = "입원 ID는 필수입니다.")
        private Long admissionId;

        @NotNull(message = "기록일은 필수입니다.")
        private LocalDate recordDate;

        private Long doctorId;

        private String condition;

        private String treatment;

        private String plan;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDailyRecordRequest {
        private String condition;

        private String treatment;

        private String plan;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateNursingNoteRequest {
        @NotNull(message = "입원 ID는 필수입니다.")
        private Long admissionId;

        private LocalDateTime recordedAt;

        @NotBlank(message = "기록한 사람은 필수입니다.")
        @Size(max = 200, message = "기록한 사람은 200자를 초과할 수 없습니다.")
        private String recordedBy;

        @NotBlank(message = "기록 유형은 필수입니다.")
        @Size(max = 50, message = "기록 유형은 50자를 초과할 수 없습니다.")
        private String noteType; // 일반, 투약, 검사, 특이사항 등

        @NotBlank(message = "기록 내용은 필수입니다.")
        private String content;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateNursingNoteRequest {
        @Size(max = 50, message = "기록 유형은 50자를 초과할 수 없습니다.")
        private String noteType;

        private String content;

        private String notes;
    }
}

