package com.emrsystem.domain.vitalsign.controller;

import com.emrsystem.domain.vitalsign.entity.VitalSign;
import com.emrsystem.domain.vitalsign.entity.VitalSignAlert;
import com.emrsystem.domain.vitalsign.facade.VitalSignFacade;
import com.emrsystem.domain.vitalsign.request.VitalSignRequests;
import com.emrsystem.domain.vitalsign.response.VitalSignResponses;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Vital Signs Monitoring", description = "생체 신호 모니터링 API")
@RestController
@RequestMapping("/api/vital-signs")
@RequiredArgsConstructor
public class VitalSignController extends BaseController {

    private final VitalSignFacade vitalSignFacade;

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "환자별 생체 신호 조회", description = "특정 환자의 생체 신호 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PageResponse<VitalSignResponses.VitalSignSummary>>> getByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<VitalSign> vitalSigns = vitalSignFacade.getByPatient(patientId, pageable);
        Page<VitalSignResponses.VitalSignSummary> summaries = vitalSigns.map(VitalSignResponses.VitalSignSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/patient/{patientId}/latest")
    @Operation(summary = "환자 최신 생체 신호 조회", description = "특정 환자의 최신 생체 신호를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<VitalSignResponses.VitalSignSummary>>> getLatestByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId) {
        List<VitalSign> vitalSigns = vitalSignFacade.getLatestByPatient(patientId);
        List<VitalSignResponses.VitalSignSummary> summaries = vitalSigns.stream()
                .map(VitalSignResponses.VitalSignSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/patient/{patientId}/date-range")
    @Operation(summary = "기간별 생체 신호 조회", description = "특정 환자의 기간별 생체 신호를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<VitalSignResponses.VitalSignSummary>>> getByDateRange(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<VitalSign> vitalSigns = vitalSignFacade.getByPatientAndDateRange(patientId, start, end);
        List<VitalSignResponses.VitalSignSummary> summaries = vitalSigns.stream()
                .map(VitalSignResponses.VitalSignSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "생체 신호 상세 조회", description = "특정 생체 신호의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<VitalSignResponses.VitalSignSummary>> get(
            @Parameter(description = "생체 신호 ID") @PathVariable Long id) {
        return ok(VitalSignResponses.VitalSignSummary.of(vitalSignFacade.get(id)));
    }

    @PostMapping
    @Operation(summary = "생체 신호 등록", description = "새로운 생체 신호를 등록합니다. 이상치 자동 감지 및 알림 생성.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<VitalSignResponses.VitalSignSummary>> create(
            @Valid @RequestBody VitalSignRequests.CreateVitalSignRequest request) {
        VitalSign vitalSign = vitalSignFacade.create(request);
        return created(VitalSignResponses.VitalSignSummary.of(vitalSign));
    }

    // VitalSignAlert endpoints
    @GetMapping("/alerts/unacknowledged")
    @Operation(summary = "미확인 알림 조회", description = "확인되지 않은 생체 신호 알림 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<VitalSignResponses.VitalSignAlertSummary>>> getUnacknowledgedAlerts(
            @Parameter(description = "심각도 필터") @RequestParam(required = false) String severity) {
        List<VitalSignAlert> alerts = severity != null
                ? vitalSignFacade.getUnacknowledgedAlertsBySeverity(severity)
                : vitalSignFacade.getUnacknowledgedAlerts();
        List<VitalSignResponses.VitalSignAlertSummary> summaries = alerts.stream()
                .map(VitalSignResponses.VitalSignAlertSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/{id}/alerts")
    @Operation(summary = "생체 신호별 알림 조회", description = "특정 생체 신호의 알림 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<VitalSignResponses.VitalSignAlertSummary>>> getAlertsByVitalSign(
            @Parameter(description = "생체 신호 ID") @PathVariable Long id) {
        List<VitalSignAlert> alerts = vitalSignFacade.getAlertsByVitalSign(id);
        List<VitalSignResponses.VitalSignAlertSummary> summaries = alerts.stream()
                .map(VitalSignResponses.VitalSignAlertSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/alerts/{alertId}/acknowledge")
    @Operation(summary = "알림 확인", description = "생체 신호 알림을 확인 처리합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<VitalSignResponses.VitalSignAlertSummary>> acknowledgeAlert(
            @Parameter(description = "알림 ID") @PathVariable Long alertId,
            @Parameter(description = "확인한 사람") @RequestParam String acknowledgedBy) {
        VitalSignAlert alert = vitalSignFacade.acknowledgeAlert(alertId, acknowledgedBy);
        return ok(VitalSignResponses.VitalSignAlertSummary.of(alert));
    }
}

