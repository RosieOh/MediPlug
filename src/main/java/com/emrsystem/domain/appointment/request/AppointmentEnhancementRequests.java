package com.emrsystem.domain.appointment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AppointmentEnhancementRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateWaitlistRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        @NotNull(message = "의사 ID는 필수입니다.")
        private Long doctorId;

        @NotNull(message = "희망 시작 시간은 필수입니다.")
        private LocalDateTime preferredStartAt;

        @NotNull(message = "희망 종료 시간은 필수입니다.")
        private LocalDateTime preferredEndAt;

        private Integer priority; // 우선순위 (낮을수록 높음, 기본값: 10)

        private String reason; // 대기 목록 등록 사유
    }

    @Getter
    @NoArgsConstructor
    public static class CreateReminderRequest {
        @NotNull(message = "예약 ID는 필수입니다.")
        private Long appointmentId;

        @NotBlank(message = "채널은 필수입니다.")
        @Size(max = 50, message = "채널은 50자를 초과할 수 없습니다.")
        private String channel; // SMS, EMAIL, PUSH

        private LocalDateTime scheduledAt; // 예약된 발송 시간 (기본값: 예약 24시간 전)

        @Size(max = 500, message = "수신자는 500자를 초과할 수 없습니다.")
        private String recipient; // 수신자 (전화번호 또는 이메일)

        private String message; // 메시지 내용 (기본값: 자동 생성)
    }
}

