package com.emrsystem.domain.schedule.controller;

import com.emrsystem.domain.schedule.entity.Shift;
import com.emrsystem.domain.schedule.entity.StaffSchedule;
import com.emrsystem.domain.schedule.facade.ScheduleFacade;
import com.emrsystem.domain.schedule.request.ScheduleRequests;
import com.emrsystem.domain.schedule.response.ScheduleResponses;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Staff Scheduling", description = "의료진 스케줄 관리 API")
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController extends BaseController {

    private final ScheduleFacade scheduleFacade;

    // StaffSchedule endpoints
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "의사별 스케줄 조회", description = "특정 의사의 스케줄을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<ScheduleResponses.StaffScheduleSummary>>> getSchedulesByDoctor(
            @Parameter(description = "의사 ID") @PathVariable Long doctorId,
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StaffSchedule> schedules = scheduleFacade.getSchedulesByDoctor(doctorId, startDate, endDate);
        List<ScheduleResponses.StaffScheduleSummary> summaries = schedules.stream()
                .map(ScheduleResponses.StaffScheduleSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "날짜별 스케줄 조회", description = "특정 날짜의 모든 스케줄을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<ScheduleResponses.StaffScheduleSummary>>> getSchedulesByDate(
            @Parameter(description = "날짜") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<StaffSchedule> schedules = scheduleFacade.getSchedulesByDate(date);
        List<ScheduleResponses.StaffScheduleSummary> summaries = schedules.stream()
                .map(ScheduleResponses.StaffScheduleSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/doctor")
    @Operation(summary = "의사 스케줄 생성", description = "의사의 근무 스케줄을 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.StaffScheduleSummary>> createDoctorSchedule(
            @Valid @RequestBody ScheduleRequests.CreateDoctorScheduleRequest request) {
        StaffSchedule schedule = scheduleFacade.createDoctorSchedule(request);
        return created(ScheduleResponses.StaffScheduleSummary.of(schedule));
    }

    @PostMapping("/staff")
    @Operation(summary = "직원 스케줄 생성", description = "직원의 근무 스케줄을 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.StaffScheduleSummary>> createStaffSchedule(
            @Valid @RequestBody ScheduleRequests.CreateStaffScheduleRequest request) {
        StaffSchedule schedule = scheduleFacade.createStaffSchedule(request);
        return created(ScheduleResponses.StaffScheduleSummary.of(schedule));
    }

    @PutMapping("/{id}")
    @Operation(summary = "스케줄 수정", description = "스케줄을 수정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.StaffScheduleSummary>> updateSchedule(
            @Parameter(description = "스케줄 ID") @PathVariable Long id,
            @Valid @RequestBody ScheduleRequests.UpdateScheduleRequest request) {
        StaffSchedule schedule = scheduleFacade.updateSchedule(id, request);
        return ok(ScheduleResponses.StaffScheduleSummary.of(schedule));
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "스케줄 확인", description = "스케줄을 확인 처리합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.StaffScheduleSummary>> confirmSchedule(
            @Parameter(description = "스케줄 ID") @PathVariable Long id) {
        StaffSchedule schedule = scheduleFacade.confirmSchedule(id);
        return ok(ScheduleResponses.StaffScheduleSummary.of(schedule));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "스케줄 취소", description = "스케줄을 취소합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.StaffScheduleSummary>> cancelSchedule(
            @Parameter(description = "스케줄 ID") @PathVariable Long id) {
        StaffSchedule schedule = scheduleFacade.cancelSchedule(id);
        return ok(ScheduleResponses.StaffScheduleSummary.of(schedule));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "스케줄 완료", description = "스케줄을 완료 처리합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.StaffScheduleSummary>> completeSchedule(
            @Parameter(description = "스케줄 ID") @PathVariable Long id) {
        StaffSchedule schedule = scheduleFacade.completeSchedule(id);
        return ok(ScheduleResponses.StaffScheduleSummary.of(schedule));
    }

    // Shift endpoints
    @GetMapping("/shifts")
    @Operation(summary = "활성 교대 근무 조회", description = "활성 상태인 교대 근무 목록을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<ScheduleResponses.ShiftSummary>>> getActiveShifts() {
        List<Shift> shifts = scheduleFacade.getActiveShifts();
        List<ScheduleResponses.ShiftSummary> summaries = shifts.stream()
                .map(ScheduleResponses.ShiftSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/shifts")
    @Operation(summary = "교대 근무 생성", description = "새로운 교대 근무를 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.ShiftSummary>> createShift(
            @Valid @RequestBody ScheduleRequests.CreateShiftRequest request) {
        Shift shift = scheduleFacade.createShift(request);
        return created(ScheduleResponses.ShiftSummary.of(shift));
    }

    @PutMapping("/shifts/{id}")
    @Operation(summary = "교대 근무 수정", description = "교대 근무를 수정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.ShiftSummary>> updateShift(
            @Parameter(description = "교대 근무 ID") @PathVariable Long id,
            @Valid @RequestBody ScheduleRequests.UpdateShiftRequest request) {
        Shift shift = scheduleFacade.updateShift(id, request);
        return ok(ScheduleResponses.ShiftSummary.of(shift));
    }

    // Statistics
    @GetMapping("/statistics")
    @Operation(summary = "스케줄 통계 조회", description = "기간별 스케줄 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<ScheduleResponses.ScheduleStatistics>> getScheduleStatistics(
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ScheduleResponses.ScheduleStatistics statistics = scheduleFacade.getScheduleStatistics(startDate, endDate);
        return ok(statistics);
    }
}

