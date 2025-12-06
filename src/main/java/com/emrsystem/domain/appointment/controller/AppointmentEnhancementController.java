package com.emrsystem.domain.appointment.controller;

import com.emrsystem.domain.appointment.entity.AppointmentHistory;
import com.emrsystem.domain.appointment.entity.AppointmentReminder;
import com.emrsystem.domain.appointment.entity.AppointmentWaitlist;
import com.emrsystem.domain.appointment.facade.AppointmentEnhancementFacade;
import com.emrsystem.domain.appointment.request.AppointmentEnhancementRequests;
import com.emrsystem.domain.appointment.response.AppointmentEnhancementResponses;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Appointment Enhancement", description = "예약 관리 강화 API")
@RestController
@RequestMapping("/api/appointments/enhancement")
@RequiredArgsConstructor
public class AppointmentEnhancementController extends BaseController {

    private final AppointmentEnhancementFacade appointmentEnhancementFacade;

    // 환자별 대기 목록 조회
    @GetMapping("/waitlist/patient/{patientId}")
    @Operation(summary = "환자별 대기 목록 조회", description = "특정 환자의 예약 대기 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<AppointmentEnhancementResponses.WaitlistSummary>>> getWaitlistsByPatient(@Parameter(description = "환자 ID") @PathVariable Long patientId) {
        List<AppointmentWaitlist> waitlists = appointmentEnhancementFacade.getWaitlistsByPatient(patientId);

        List<AppointmentEnhancementResponses.WaitlistSummary> summaries = waitlists.stream()
                .map(AppointmentEnhancementResponses.WaitlistSummary::of)
                .collect(Collectors.toList());

        return ok(summaries);
    }

    @GetMapping("/waitlist/doctor/{doctorId}")
    @Operation(summary = "의사별 대기 목록 조회", description = "특정 의사의 예약 대기 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<AppointmentEnhancementResponses.WaitlistSummary>>> getWaitlistsByDoctor(@Parameter(description = "의사 ID") @PathVariable Long doctorId) {
        List<AppointmentWaitlist> waitlists = appointmentEnhancementFacade.getWaitlistsByDoctor(doctorId);

        List<AppointmentEnhancementResponses.WaitlistSummary> summaries = waitlists.stream()
                .map(AppointmentEnhancementResponses.WaitlistSummary::of)
                .collect(Collectors.toList());

        return ok(summaries);
    }

    @GetMapping("/waitlist/pending")
    @Operation(summary = "대기 중인 목록 조회", description = "대기 중인 예약 목록을 우선순위순으로 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<AppointmentEnhancementResponses.WaitlistSummary>>> getPendingWaitlists() {
        List<AppointmentWaitlist> waitlists = appointmentEnhancementFacade.getPendingWaitlists();

        List<AppointmentEnhancementResponses.WaitlistSummary> summaries = waitlists.stream()
                .map(AppointmentEnhancementResponses.WaitlistSummary::of)
                .collect(Collectors.toList());

        return ok(summaries);
    }

    @PostMapping("/waitlist")
    @Operation(summary = "대기 목록 추가", description = "예약 대기 목록에 환자를 추가합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.WaitlistSummary>> addToWaitlist(@Valid @RequestBody AppointmentEnhancementRequests.CreateWaitlistRequest request) {
        AppointmentWaitlist waitlist = appointmentEnhancementFacade.addToWaitlist(request);

        return created(AppointmentEnhancementResponses.WaitlistSummary.of(waitlist));
    }

    @PostMapping("/waitlist/{waitlistId}/notify")
    @Operation(summary = "대기 목록 알림", description = "대기 목록 환자에게 예약 가능 알림을 보냅니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.WaitlistSummary>> notifyWaitlist(@Parameter(description = "대기 목록 ID") @PathVariable Long waitlistId) {
        AppointmentWaitlist waitlist = appointmentEnhancementFacade.notifyWaitlist(waitlistId);

        return ok(AppointmentEnhancementResponses.WaitlistSummary.of(waitlist));
    }

    @PostMapping("/waitlist/{waitlistId}/fulfill")
    @Operation(summary = "대기 목록 처리", description = "대기 목록을 예약으로 전환합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.WaitlistSummary>> fulfillWaitlist(@Parameter(description = "대기 목록 ID") @PathVariable Long waitlistId,
                                                                                                        @Parameter(description = "예약 ID") @RequestParam Long appointmentId) {
        AppointmentWaitlist waitlist = appointmentEnhancementFacade.fulfillWaitlist(waitlistId, appointmentId);

        return ok(AppointmentEnhancementResponses.WaitlistSummary.of(waitlist));
    }

    @PostMapping("/waitlist/{waitlistId}/cancel")
    @Operation(summary = "대기 목록 취소", description = "대기 목록을 취소합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.WaitlistSummary>> cancelWaitlist(@Parameter(description = "대기 목록 ID") @PathVariable Long waitlistId) {
        AppointmentWaitlist waitlist = appointmentEnhancementFacade.cancelWaitlist(waitlistId);

        return ok(AppointmentEnhancementResponses.WaitlistSummary.of(waitlist));
    }

    // 예약 이력 조회
    @GetMapping("/history/{appointmentId}")
    @Operation(summary = "예약 이력 조회", description = "특정 예약의 변경 이력을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<AppointmentEnhancementResponses.AppointmentHistorySummary>>> getAppointmentHistory(@Parameter(description = "예약 ID") @PathVariable Long appointmentId) {
        List<AppointmentHistory> histories = appointmentEnhancementFacade.getAppointmentHistory(appointmentId);
        List<AppointmentEnhancementResponses.AppointmentHistorySummary> summaries = histories.stream()
                .map(AppointmentEnhancementResponses.AppointmentHistorySummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    // Reminder endpoints
    @GetMapping("/reminders/appointment/{appointmentId}")
    @Operation(summary = "예약별 리마인더 조회", description = "특정 예약의 리마인더 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<AppointmentEnhancementResponses.ReminderSummary>>> getRemindersByAppointment(
            @Parameter(description = "예약 ID") @PathVariable Long appointmentId) {
        List<AppointmentReminder> reminders = appointmentEnhancementFacade.getRemindersByAppointment(appointmentId);
        List<AppointmentEnhancementResponses.ReminderSummary> summaries = reminders.stream()
                .map(AppointmentEnhancementResponses.ReminderSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/reminders/pending")
    @Operation(summary = "대기 중인 리마인더 조회", description = "발송 대기 중인 리마인더 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<AppointmentEnhancementResponses.ReminderSummary>>> getPendingReminders() {
        List<AppointmentReminder> reminders = appointmentEnhancementFacade.getPendingReminders();
        List<AppointmentEnhancementResponses.ReminderSummary> summaries = reminders.stream()
                .map(AppointmentEnhancementResponses.ReminderSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/reminders")
    @Operation(summary = "리마인더 생성", description = "예약 리마인더를 생성합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.ReminderSummary>> createReminder(
            @Valid @RequestBody AppointmentEnhancementRequests.CreateReminderRequest request) {
        AppointmentReminder reminder = appointmentEnhancementFacade.createReminder(request);
        return created(AppointmentEnhancementResponses.ReminderSummary.of(reminder));
    }

    @PostMapping("/reminders/{reminderId}/sent")
    @Operation(summary = "리마인더 발송 완료", description = "리마인더 발송을 완료 처리합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.ReminderSummary>> markReminderAsSent(
            @Parameter(description = "리마인더 ID") @PathVariable Long reminderId) {
        AppointmentReminder reminder = appointmentEnhancementFacade.markReminderAsSent(reminderId);
        return ok(AppointmentEnhancementResponses.ReminderSummary.of(reminder));
    }

    @PostMapping("/reminders/{reminderId}/failed")
    @Operation(summary = "리마인더 발송 실패", description = "리마인더 발송 실패를 기록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.ReminderSummary>> markReminderAsFailed(
            @Parameter(description = "리마인더 ID") @PathVariable Long reminderId,
            @Parameter(description = "오류 메시지") @RequestParam String errorMessage) {
        AppointmentReminder reminder = appointmentEnhancementFacade.markReminderAsFailed(reminderId, errorMessage);
        return ok(AppointmentEnhancementResponses.ReminderSummary.of(reminder));
    }

    // Statistics endpoints
    @GetMapping("/statistics")
    @Operation(summary = "예약 통계 조회", description = "기간별 예약 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<AppointmentEnhancementResponses.AppointmentStatistics>> getAppointmentStatistics(
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        AppointmentEnhancementResponses.AppointmentStatistics statistics = 
                appointmentEnhancementFacade.getAppointmentStatistics(startDate, endDate);
        return ok(statistics);
    }
}

