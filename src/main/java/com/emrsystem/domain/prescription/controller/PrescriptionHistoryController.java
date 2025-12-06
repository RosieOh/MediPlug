package com.emrsystem.domain.prescription.controller;

import com.emrsystem.domain.prescription.entity.MedicationLog;
import com.emrsystem.domain.prescription.facade.PrescriptionHistoryFacade;
import com.emrsystem.domain.prescription.request.PrescriptionHistoryRequests;
import com.emrsystem.domain.prescription.response.PrescriptionHistoryResponses;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Prescription History", description = "처방 이력 관리 API")
@RestController
@RequestMapping("/api/prescription-history")
@RequiredArgsConstructor
public class PrescriptionHistoryController extends BaseController {

    private final PrescriptionHistoryFacade prescriptionHistoryFacade;

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "환자별 처방 이력 조회", description = "특정 환자의 처방 이력을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PageResponse<PrescriptionHistoryResponses.PrescriptionSummary>>> getPrescriptionHistoryByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<PrescriptionHistoryResponses.PrescriptionSummary> prescriptions = 
                prescriptionHistoryFacade.getPrescriptionHistoryByPatient(patientId, pageable);
        return ok(PageResponse.of(prescriptions));
    }

    @GetMapping("/prescription/{prescriptionId}/history")
    @Operation(summary = "처방 변경 이력 조회", description = "특정 처방의 변경 이력을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<PrescriptionHistoryResponses.PrescriptionHistorySummary>>> getPrescriptionChangeHistory(
            @Parameter(description = "처방 ID") @PathVariable Long prescriptionId) {
        List<PrescriptionHistoryResponses.PrescriptionHistorySummary> histories = 
                prescriptionHistoryFacade.getPrescriptionChangeHistory(prescriptionId);
        return ok(histories);
    }

    @GetMapping("/duplicate-check")
    @Operation(summary = "약물 중복 처방 체크", description = "특정 기간 내 동일 약물의 중복 처방 여부를 확인합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PrescriptionHistoryResponses.DuplicatePrescriptionCheck>> checkDuplicatePrescription(
            @Parameter(description = "환자 ID") @RequestParam Long patientId,
            @Parameter(description = "약물 코드") @RequestParam String drugCode,
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        PrescriptionHistoryResponses.DuplicatePrescriptionCheck check = 
                prescriptionHistoryFacade.checkDuplicatePrescription(patientId, drugCode, startDate, endDate);
        return ok(check);
    }

    @GetMapping("/medication-logs/patient/{patientId}")
    @Operation(summary = "환자별 약물 복용 이력 조회", description = "특정 환자의 약물 복용 이력을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<PrescriptionHistoryResponses.MedicationLogSummary>>> getMedicationLogsByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PrescriptionHistoryResponses.MedicationLogSummary> logs = 
                prescriptionHistoryFacade.getMedicationLogsByPatient(patientId, startDate, endDate);
        return ok(logs);
    }

    @GetMapping("/medication-logs/prescription/{prescriptionId}")
    @Operation(summary = "처방별 약물 복용 이력 조회", description = "특정 처방의 약물 복용 이력을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<PrescriptionHistoryResponses.MedicationLogSummary>>> getMedicationLogsByPrescription(
            @Parameter(description = "처방 ID") @PathVariable Long prescriptionId,
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PrescriptionHistoryResponses.MedicationLogSummary> logs = 
                prescriptionHistoryFacade.getMedicationLogsByPrescription(prescriptionId, startDate, endDate);
        return ok(logs);
    }

    @PostMapping("/medication-logs")
    @Operation(summary = "약물 복용 기록 등록", description = "새로운 약물 복용 기록을 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PrescriptionHistoryResponses.MedicationLogSummary>> createMedicationLog(
            @Valid @RequestBody PrescriptionHistoryRequests.CreateMedicationLogRequest request) {
        MedicationLog log = prescriptionHistoryFacade.createMedicationLog(request);
        return created(PrescriptionHistoryResponses.MedicationLogSummary.of(log));
    }

    @PutMapping("/medication-logs/{logId}")
    @Operation(summary = "약물 복용 기록 수정", description = "약물 복용 기록을 수정합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PrescriptionHistoryResponses.MedicationLogSummary>> updateMedicationLog(
            @Parameter(description = "복용 기록 ID") @PathVariable Long logId,
            @Valid @RequestBody PrescriptionHistoryRequests.UpdateMedicationLogRequest request) {
        MedicationLog log = prescriptionHistoryFacade.updateMedicationLog(logId, request);
        return ok(PrescriptionHistoryResponses.MedicationLogSummary.of(log));
    }
}

