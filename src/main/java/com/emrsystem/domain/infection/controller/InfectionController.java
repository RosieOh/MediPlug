package com.emrsystem.domain.infection.controller;

import com.emrsystem.domain.infection.entity.InfectionCase;
import com.emrsystem.domain.infection.entity.InfectionPrevention;
import com.emrsystem.domain.infection.facade.InfectionFacade;
import com.emrsystem.domain.infection.request.InfectionRequests;
import com.emrsystem.domain.infection.response.InfectionResponses;
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
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Infection Control", description = "감염 관리 API")
@RestController
@RequestMapping("/api/infections")
@RequiredArgsConstructor
public class InfectionController extends BaseController {

    private final InfectionFacade infectionFacade;

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "환자별 감염 사례 조회", description = "특정 환자의 감염 사례 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PageResponse<InfectionResponses.InfectionCaseSummary>>> getByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<InfectionCase> cases = infectionFacade.getByPatient(patientId, pageable);
        Page<InfectionResponses.InfectionCaseSummary> summaries = cases.map(InfectionResponses.InfectionCaseSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 감염 사례 조회", description = "특정 상태의 감염 사례 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InfectionResponses.InfectionCaseSummary>>> getByStatus(
            @Parameter(description = "상태") @PathVariable String status) {
        List<InfectionCase> cases = infectionFacade.getByStatus(status);
        List<InfectionResponses.InfectionCaseSummary> summaries = cases.stream()
                .map(InfectionResponses.InfectionCaseSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/type/{infectionType}")
    @Operation(summary = "유형별 감염 사례 조회", description = "특정 유형의 감염 사례 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InfectionResponses.InfectionCaseSummary>>> getByType(
            @Parameter(description = "감염 유형") @PathVariable String infectionType) {
        List<InfectionCase> cases = infectionFacade.getByType(infectionType);
        List<InfectionResponses.InfectionCaseSummary> summaries = cases.stream()
                .map(InfectionResponses.InfectionCaseSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/date-range")
    @Operation(summary = "기간별 감염 사례 조회", description = "특정 기간의 감염 사례 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InfectionResponses.InfectionCaseSummary>>> getByDateRange(
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<InfectionCase> cases = infectionFacade.getByDateRange(start, end);
        List<InfectionResponses.InfectionCaseSummary> summaries = cases.stream()
                .map(InfectionResponses.InfectionCaseSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/isolation-room/{isolationRoom}")
    @Operation(summary = "격리실별 감염 사례 조회", description = "특정 격리실의 활성 감염 사례를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InfectionResponses.InfectionCaseSummary>>> getByIsolationRoom(
            @Parameter(description = "격리실") @PathVariable String isolationRoom) {
        List<InfectionCase> cases = infectionFacade.getByIsolationRoom(isolationRoom);
        List<InfectionResponses.InfectionCaseSummary> summaries = cases.stream()
                .map(InfectionResponses.InfectionCaseSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "감염 사례 상세 조회", description = "특정 감염 사례의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionCaseDetail>> get(
            @Parameter(description = "감염 사례 ID") @PathVariable Long id) {
        InfectionCase infectionCase = infectionFacade.get(id);
        List<InfectionPrevention> preventions = infectionFacade.getPreventions(id);
        return ok(InfectionResponses.InfectionCaseDetail.of(infectionCase, preventions));
    }

    @PostMapping
    @Operation(summary = "감염 사례 등록", description = "새로운 감염 사례를 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionCaseSummary>> create(
            @Valid @RequestBody InfectionRequests.CreateInfectionCaseRequest request) {
        InfectionCase infectionCase = infectionFacade.create(request);
        return created(InfectionResponses.InfectionCaseSummary.of(infectionCase));
    }

    @PutMapping("/{id}")
    @Operation(summary = "감염 사례 수정", description = "감염 사례 정보를 수정합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionCaseSummary>> update(
            @Parameter(description = "감염 사례 ID") @PathVariable Long id,
            @Valid @RequestBody InfectionRequests.UpdateInfectionCaseRequest request) {
        InfectionCase infectionCase = infectionFacade.update(id, request);
        return ok(InfectionResponses.InfectionCaseSummary.of(infectionCase));
    }

    @PostMapping("/{id}/resolve")
    @Operation(summary = "감염 해소", description = "감염 사례를 해소 처리합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionCaseSummary>> resolve(
            @Parameter(description = "감염 사례 ID") @PathVariable Long id,
            @Parameter(description = "해소일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate resolvedDate) {
        InfectionCase infectionCase = infectionFacade.resolve(id, resolvedDate);
        return ok(InfectionResponses.InfectionCaseSummary.of(infectionCase));
    }

    @PostMapping("/{id}/monitoring")
    @Operation(summary = "감염 모니터링 설정", description = "감염 사례를 모니터링 상태로 변경합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionCaseSummary>> setMonitoring(
            @Parameter(description = "감염 사례 ID") @PathVariable Long id) {
        InfectionCase infectionCase = infectionFacade.setMonitoring(id);
        return ok(InfectionResponses.InfectionCaseSummary.of(infectionCase));
    }

    // InfectionPrevention endpoints
    @GetMapping("/{id}/preventions")
    @Operation(summary = "예방 조치 조회", description = "감염 사례의 예방 조치 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InfectionResponses.InfectionPreventionSummary>>> getPreventions(
            @Parameter(description = "감염 사례 ID") @PathVariable Long id) {
        List<InfectionPrevention> preventions = infectionFacade.getPreventions(id);
        List<InfectionResponses.InfectionPreventionSummary> summaries = preventions.stream()
                .map(InfectionResponses.InfectionPreventionSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/preventions")
    @Operation(summary = "예방 조치 등록", description = "새로운 예방 조치를 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionPreventionSummary>> createPrevention(
            @Valid @RequestBody InfectionRequests.CreatePreventionRequest request) {
        InfectionPrevention prevention = infectionFacade.createPrevention(request);
        return created(InfectionResponses.InfectionPreventionSummary.of(prevention));
    }

    @PostMapping("/preventions/{preventionId}/deactivate")
    @Operation(summary = "예방 조치 비활성화", description = "예방 조치를 비활성화합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InfectionResponses.InfectionPreventionSummary>> deactivatePrevention(
            @Parameter(description = "예방 조치 ID") @PathVariable Long preventionId) {
        InfectionPrevention prevention = infectionFacade.deactivatePrevention(preventionId);
        return ok(InfectionResponses.InfectionPreventionSummary.of(prevention));
    }
}

