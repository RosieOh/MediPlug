package com.emrsystem.domain.inpatient.controller;

import com.emrsystem.domain.inpatient.entity.InpatientDailyRecord;
import com.emrsystem.domain.inpatient.entity.NursingNote;
import com.emrsystem.domain.inpatient.facade.InpatientFacade;
import com.emrsystem.domain.inpatient.request.InpatientRequests;
import com.emrsystem.domain.inpatient.response.InpatientResponses;
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
import java.util.stream.Collectors;

@Tag(name = "Inpatient Management", description = "입원 환자 관리 API")
@RestController
@RequestMapping("/api/inpatients")
@RequiredArgsConstructor
public class InpatientController extends BaseController {

    private final InpatientFacade inpatientFacade;

    // InpatientDailyRecord endpoints
    @GetMapping("/admissions/{admissionId}/daily-records")
    @Operation(summary = "입원별 일일 기록 조회", description = "특정 입원의 일일 기록 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InpatientResponses.InpatientDailyRecordSummary>>> getDailyRecordsByAdmission(
            @Parameter(description = "입원 ID") @PathVariable Long admissionId) {
        List<InpatientDailyRecord> records = inpatientFacade.getDailyRecordsByAdmission(admissionId);
        List<InpatientResponses.InpatientDailyRecordSummary> summaries = records.stream()
                .map(InpatientResponses.InpatientDailyRecordSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/admissions/{admissionId}/daily-records/date")
    @Operation(summary = "날짜별 일일 기록 조회", description = "특정 입원의 특정 날짜 일일 기록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InpatientResponses.InpatientDailyRecordSummary>>> getDailyRecordsByDate(
            @Parameter(description = "입원 ID") @PathVariable Long admissionId,
            @Parameter(description = "기록일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        List<InpatientDailyRecord> records = inpatientFacade.getDailyRecordsByAdmissionAndDate(admissionId, recordDate);
        List<InpatientResponses.InpatientDailyRecordSummary> summaries = records.stream()
                .map(InpatientResponses.InpatientDailyRecordSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/daily-records/{id}")
    @Operation(summary = "일일 기록 상세 조회", description = "특정 일일 기록의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InpatientResponses.InpatientDailyRecordSummary>> getDailyRecord(
            @Parameter(description = "일일 기록 ID") @PathVariable Long id) {
        return ok(InpatientResponses.InpatientDailyRecordSummary.of(inpatientFacade.getDailyRecord(id)));
    }

    @PostMapping("/daily-records")
    @Operation(summary = "일일 기록 등록", description = "새로운 일일 기록을 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InpatientResponses.InpatientDailyRecordSummary>> createDailyRecord(
            @Valid @RequestBody InpatientRequests.CreateDailyRecordRequest request) {
        InpatientDailyRecord record = inpatientFacade.createDailyRecord(request);
        return created(InpatientResponses.InpatientDailyRecordSummary.of(record));
    }

    @PutMapping("/daily-records/{id}")
    @Operation(summary = "일일 기록 수정", description = "일일 기록을 수정합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InpatientResponses.InpatientDailyRecordSummary>> updateDailyRecord(
            @Parameter(description = "일일 기록 ID") @PathVariable Long id,
            @Valid @RequestBody InpatientRequests.UpdateDailyRecordRequest request) {
        InpatientDailyRecord record = inpatientFacade.updateDailyRecord(id, request);
        return ok(InpatientResponses.InpatientDailyRecordSummary.of(record));
    }

    // NursingNote endpoints
    @GetMapping("/admissions/{admissionId}/nursing-notes")
    @Operation(summary = "입원별 간병 기록 조회", description = "특정 입원의 간병 기록 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PageResponse<InpatientResponses.NursingNoteSummary>>> getNursingNotesByAdmission(
            @Parameter(description = "입원 ID") @PathVariable Long admissionId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<NursingNote> notes = inpatientFacade.getNursingNotesByAdmission(admissionId, pageable);
        Page<InpatientResponses.NursingNoteSummary> summaries = notes.map(InpatientResponses.NursingNoteSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/admissions/{admissionId}/nursing-notes/date-range")
    @Operation(summary = "기간별 간병 기록 조회", description = "특정 입원의 기간별 간병 기록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InpatientResponses.NursingNoteSummary>>> getNursingNotesByDateRange(
            @Parameter(description = "입원 ID") @PathVariable Long admissionId,
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<NursingNote> notes = inpatientFacade.getNursingNotesByAdmissionAndDateRange(admissionId, start, end);
        List<InpatientResponses.NursingNoteSummary> summaries = notes.stream()
                .map(InpatientResponses.NursingNoteSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/admissions/{admissionId}/nursing-notes/type/{noteType}")
    @Operation(summary = "유형별 간병 기록 조회", description = "특정 입원의 특정 유형 간병 기록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<InpatientResponses.NursingNoteSummary>>> getNursingNotesByType(
            @Parameter(description = "입원 ID") @PathVariable Long admissionId,
            @Parameter(description = "기록 유형") @PathVariable String noteType) {
        List<NursingNote> notes = inpatientFacade.getNursingNotesByAdmissionAndType(admissionId, noteType);
        List<InpatientResponses.NursingNoteSummary> summaries = notes.stream()
                .map(InpatientResponses.NursingNoteSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/nursing-notes/{id}")
    @Operation(summary = "간병 기록 상세 조회", description = "특정 간병 기록의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<InpatientResponses.NursingNoteSummary>> getNursingNote(
            @Parameter(description = "간병 기록 ID") @PathVariable Long id) {
        return ok(InpatientResponses.NursingNoteSummary.of(inpatientFacade.getNursingNote(id)));
    }

    @PostMapping("/nursing-notes")
    @Operation(summary = "간병 기록 등록", description = "새로운 간병 기록을 등록합니다.")
    @RequireRole(RoleType.NURSE)
    public ResponseEntity<ApiResponse<InpatientResponses.NursingNoteSummary>> createNursingNote(
            @Valid @RequestBody InpatientRequests.CreateNursingNoteRequest request) {
        NursingNote note = inpatientFacade.createNursingNote(request);
        return created(InpatientResponses.NursingNoteSummary.of(note));
    }

    @PutMapping("/nursing-notes/{id}")
    @Operation(summary = "간병 기록 수정", description = "간병 기록을 수정합니다.")
    @RequireRole(RoleType.NURSE)
    public ResponseEntity<ApiResponse<InpatientResponses.NursingNoteSummary>> updateNursingNote(
            @Parameter(description = "간병 기록 ID") @PathVariable Long id,
            @Valid @RequestBody InpatientRequests.UpdateNursingNoteRequest request) {
        NursingNote note = inpatientFacade.updateNursingNote(id, request);
        return ok(InpatientResponses.NursingNoteSummary.of(note));
    }
}

