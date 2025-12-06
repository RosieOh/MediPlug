package com.emrsystem.domain.bed.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BedRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateBedRequest {
        @NotNull(message = "병실 ID는 필수입니다.")
        private Long roomId;

        @NotBlank(message = "병상번호는 필수입니다.")
        @Size(max = 50, message = "병상번호는 50자를 초과할 수 없습니다.")
        private String bedNumber;

        @NotBlank(message = "병상 유형은 필수입니다.")
        @Size(max = 50, message = "병상 유형은 50자를 초과할 수 없습니다.")
        private String bedType;

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateBedRequest {
        @NotBlank(message = "병상 유형은 필수입니다.")
        @Size(max = 50, message = "병상 유형은 50자를 초과할 수 없습니다.")
        private String bedType;

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateAdmissionRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        @NotNull(message = "병상 ID는 필수입니다.")
        private Long bedId;

        @NotNull(message = "입원일은 필수입니다.")
        private LocalDateTime admissionDate;

        @NotBlank(message = "입원 사유는 필수입니다.")
        @Size(max = 1000, message = "입원 사유는 1000자를 초과할 수 없습니다.")
        private String reason;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateAdmissionRequest {
        @Size(max = 1000, message = "입원 사유는 1000자를 초과할 수 없습니다.")
        private String reason;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class DischargeRequest {
        @NotNull(message = "퇴원일은 필수입니다.")
        private LocalDateTime dischargeDate;

        @NotBlank(message = "퇴원 사유는 필수입니다.")
        @Size(max = 1000, message = "퇴원 사유는 1000자를 초과할 수 없습니다.")
        private String reason;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class TransferRequest {
        @NotNull(message = "이전할 병상 ID는 필수입니다.")
        private Long newBedId;

        @NotNull(message = "이전일은 필수입니다.")
        private LocalDateTime transferDate;

        @NotBlank(message = "이전 사유는 필수입니다.")
        @Size(max = 1000, message = "이전 사유는 1000자를 초과할 수 없습니다.")
        private String reason;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class BedAvailabilityRequest {
        @Size(max = 50, message = "병상 유형은 50자를 초과할 수 없습니다.")
        private String bedType;

        @Size(max = 100, message = "병실 코드는 100자를 초과할 수 없습니다.")
        private String roomCode;
    }
}
