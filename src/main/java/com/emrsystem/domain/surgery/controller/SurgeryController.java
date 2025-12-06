package com.emrsystem.domain.surgery.controller;

import com.emrsystem.domain.surgery.entity.PreOpChecklist;
import com.emrsystem.domain.surgery.entity.Surgery;
import com.emrsystem.domain.surgery.entity.SurgeryTeam;
import com.emrsystem.domain.surgery.facade.SurgeryFacade;
import com.emrsystem.domain.surgery.request.SurgeryRequests;
import com.emrsystem.domain.surgery.response.SurgeryResponses;
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

@Tag(name = "Surgery Management", description = "수술 관리 API")
@RestController
@RequestMapping("/api/surgeries")
@RequiredArgsConstructor
public class SurgeryController extends BaseController {

    private final SurgeryFacade surgeryFacade;

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "환자별 수술 목록 조회", description = "특정 환자의 수술 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PageResponse<SurgeryResponses.SurgerySummary>>> getByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<Surgery> surgeries = surgeryFacade.getByPatient(patientId, pageable);
        Page<SurgeryResponses.SurgerySummary> summaries = surgeries.map(SurgeryResponses.SurgerySummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/surgeon/{surgeonId}")
    @Operation(summary = "의사별 수술 목록 조회", description = "특정 의사의 수술 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<SurgeryResponses.SurgerySummary>>> getBySurgeon(
            @Parameter(description = "의사 ID") @PathVariable Long surgeonId) {
        List<Surgery> surgeries = surgeryFacade.getBySurgeon(surgeonId);
        List<SurgeryResponses.SurgerySummary> summaries = surgeries.stream()
                .map(SurgeryResponses.SurgerySummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/date-range")
    @Operation(summary = "기간별 수술 목록 조회", description = "특정 기간의 수술 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<SurgeryResponses.SurgerySummary>>> getByDateRange(
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Surgery> surgeries = surgeryFacade.getByDateRange(start, end);
        List<SurgeryResponses.SurgerySummary> summaries = surgeries.stream()
                .map(SurgeryResponses.SurgerySummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 수술 목록 조회", description = "특정 상태의 수술 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<SurgeryResponses.SurgerySummary>>> getByStatus(
            @Parameter(description = "상태") @PathVariable String status) {
        List<Surgery> surgeries = surgeryFacade.getByStatus(status);
        List<SurgeryResponses.SurgerySummary> summaries = surgeries.stream()
                .map(SurgeryResponses.SurgerySummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "수술 상세 조회", description = "특정 수술의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgeryDetail>> get(
            @Parameter(description = "수술 ID") @PathVariable Long id) {
        Surgery surgery = surgeryFacade.get(id);
        List<SurgeryTeam> teamMembers = surgeryFacade.getTeamMembers(id);
        List<PreOpChecklist> checklist = surgeryFacade.getChecklist(id);
        return ok(SurgeryResponses.SurgeryDetail.of(surgery, teamMembers, checklist));
    }

    @PostMapping
    @Operation(summary = "수술 등록", description = "새로운 수술을 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> create(
            @Valid @RequestBody SurgeryRequests.CreateSurgeryRequest request) {
        Surgery surgery = surgeryFacade.create(request);
        return created(SurgeryResponses.SurgerySummary.of(surgery));
    }

    @PutMapping("/{id}")
    @Operation(summary = "수술 정보 수정", description = "수술 정보를 수정합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> update(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Valid @RequestBody SurgeryRequests.UpdateSurgeryRequest request) {
        Surgery surgery = surgeryFacade.update(id, request);
        return ok(SurgeryResponses.SurgerySummary.of(surgery));
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "수술 시작", description = "수술을 시작합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> start(
            @Parameter(description = "수술 ID") @PathVariable Long id) {
        Surgery surgery = surgeryFacade.start(id);
        return ok(SurgeryResponses.SurgerySummary.of(surgery));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "수술 완료", description = "수술을 완료 처리합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> complete(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Valid @RequestBody SurgeryRequests.CompleteSurgeryRequest request) {
        Surgery surgery = surgeryFacade.complete(id, request);
        return ok(SurgeryResponses.SurgerySummary.of(surgery));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "수술 취소", description = "수술을 취소합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> cancel(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Parameter(description = "취소 사유") @RequestParam String reason) {
        Surgery surgery = surgeryFacade.cancel(id, reason);
        return ok(SurgeryResponses.SurgerySummary.of(surgery));
    }

    @PostMapping("/{id}/postpone")
    @Operation(summary = "수술 연기", description = "수술을 연기합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> postpone(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Parameter(description = "새 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDateTime,
            @Parameter(description = "연기 사유") @RequestParam String reason) {
        Surgery surgery = surgeryFacade.postpone(id, newDateTime, reason);
        return ok(SurgeryResponses.SurgerySummary.of(surgery));
    }

    @PostMapping("/{id}/complication")
    @Operation(summary = "합병증 추가", description = "수술 합병증을 추가합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgerySummary>> addComplication(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Valid @RequestBody SurgeryRequests.AddComplicationRequest request) {
        Surgery surgery = surgeryFacade.addComplication(id, request);
        return ok(SurgeryResponses.SurgerySummary.of(surgery));
    }

    // SurgeryTeam endpoints
    @GetMapping("/{id}/team")
    @Operation(summary = "수술팀 조회", description = "수술팀 구성원을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<SurgeryResponses.SurgeryTeamSummary>>> getTeamMembers(
            @Parameter(description = "수술 ID") @PathVariable Long id) {
        List<SurgeryTeam> teamMembers = surgeryFacade.getTeamMembers(id);
        List<SurgeryResponses.SurgeryTeamSummary> summaries = teamMembers.stream()
                .map(SurgeryResponses.SurgeryTeamSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/{id}/team")
    @Operation(summary = "수술팀 구성원 추가", description = "수술팀 구성원을 추가합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.SurgeryTeamSummary>> addTeamMember(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Valid @RequestBody SurgeryRequests.TeamMemberRequest request) {
        SurgeryTeam team = surgeryFacade.addTeamMember(id, request);
        return created(SurgeryResponses.SurgeryTeamSummary.of(team));
    }

    @DeleteMapping("/team/{teamId}")
    @Operation(summary = "수술팀 구성원 제거", description = "수술팀 구성원을 제거합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<String>> removeTeamMember(
            @Parameter(description = "팀 ID") @PathVariable Long teamId) {
        surgeryFacade.removeTeamMember(teamId);
        return okMessage("removed");
    }

    // PreOpChecklist endpoints
    @GetMapping("/{id}/checklist")
    @Operation(summary = "수술 전 체크리스트 조회", description = "수술 전 체크리스트를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<SurgeryResponses.PreOpChecklistSummary>>> getChecklist(
            @Parameter(description = "수술 ID") @PathVariable Long id) {
        List<PreOpChecklist> checklist = surgeryFacade.getChecklist(id);
        List<SurgeryResponses.PreOpChecklistSummary> summaries = checklist.stream()
                .map(SurgeryResponses.PreOpChecklistSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/{id}/checklist")
    @Operation(summary = "체크리스트 항목 추가", description = "수술 전 체크리스트 항목을 추가합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.PreOpChecklistSummary>> addChecklistItem(
            @Parameter(description = "수술 ID") @PathVariable Long id,
            @Parameter(description = "항목") @RequestParam String item) {
        PreOpChecklist checklist = surgeryFacade.addChecklistItem(id, item);
        return created(SurgeryResponses.PreOpChecklistSummary.of(checklist));
    }

    @PostMapping("/checklist/{checklistId}/check")
    @Operation(summary = "체크리스트 항목 체크", description = "체크리스트 항목을 체크합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<SurgeryResponses.PreOpChecklistSummary>> checkItem(
            @Parameter(description = "체크리스트 ID") @PathVariable Long checklistId,
            @Parameter(description = "체크한 사람") @RequestParam String checkedBy) {
        PreOpChecklist checklist = surgeryFacade.checkItem(checklistId, checkedBy);
        return ok(SurgeryResponses.PreOpChecklistSummary.of(checklist));
    }

    @DeleteMapping("/checklist/{checklistId}")
    @Operation(summary = "체크리스트 항목 삭제", description = "체크리스트 항목을 삭제합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<String>> removeChecklistItem(
            @Parameter(description = "체크리스트 ID") @PathVariable Long checklistId) {
        surgeryFacade.removeChecklistItem(checklistId);
        return okMessage("removed");
    }
}

