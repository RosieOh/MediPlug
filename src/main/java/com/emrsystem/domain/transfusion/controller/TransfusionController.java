package com.emrsystem.domain.transfusion.controller;

import com.emrsystem.domain.transfusion.entity.TransfusionRecord;
import com.emrsystem.domain.transfusion.entity.TransfusionReaction;
import com.emrsystem.domain.transfusion.entity.TransfusionRequest;
import com.emrsystem.domain.transfusion.facade.TransfusionFacade;
import com.emrsystem.domain.transfusion.request.TransfusionRequests;
import com.emrsystem.domain.transfusion.response.TransfusionResponses;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Transfusion Management", description = "수혈 관리 API")
@RestController
@RequestMapping("/api/transfusions")
@RequiredArgsConstructor
public class TransfusionController extends BaseController {

    private final TransfusionFacade transfusionFacade;

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "환자별 수혈 요청 조회", description = "특정 환자의 수혈 요청 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<PageResponse<TransfusionResponses.TransfusionRequestSummary>>> getRequestsByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<TransfusionRequest> requests = transfusionFacade.getRequestsByPatient(patientId, pageable);
        Page<TransfusionResponses.TransfusionRequestSummary> summaries = requests.map(TransfusionResponses.TransfusionRequestSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 수혈 요청 조회", description = "특정 상태의 수혈 요청 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<TransfusionResponses.TransfusionRequestSummary>>> getRequestsByStatus(
            @Parameter(description = "상태") @PathVariable String status) {
        List<TransfusionRequest> requests = transfusionFacade.getRequestsByStatus(status);
        List<TransfusionResponses.TransfusionRequestSummary> summaries = requests.stream()
                .map(TransfusionResponses.TransfusionRequestSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/requests/{id}")
    @Operation(summary = "수혈 요청 상세 조회", description = "특정 수혈 요청의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionRequestSummary>> getRequest(
            @Parameter(description = "수혈 요청 ID") @PathVariable Long id) {
        return ok(TransfusionResponses.TransfusionRequestSummary.of(transfusionFacade.getRequest(id)));
    }

    @PostMapping("/requests")
    @Operation(summary = "수혈 요청 등록", description = "새로운 수혈 요청을 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionRequestSummary>> createRequest(
            @Valid @RequestBody TransfusionRequests.CreateTransfusionRequest request) {
        TransfusionRequest transfusionRequest = transfusionFacade.createRequest(request);
        return created(TransfusionResponses.TransfusionRequestSummary.of(transfusionRequest));
    }

    @PostMapping("/requests/{id}/approve")
    @Operation(summary = "수혈 요청 승인", description = "수혈 요청을 승인합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionRequestSummary>> approveRequest(
            @Parameter(description = "수혈 요청 ID") @PathVariable Long id,
            @Parameter(description = "승인 의사 ID") @RequestParam Long approvingDoctorId) {
        TransfusionRequest request = transfusionFacade.approveRequest(id, approvingDoctorId);
        return ok(TransfusionResponses.TransfusionRequestSummary.of(request));
    }

    @PostMapping("/requests/{id}/reject")
    @Operation(summary = "수혈 요청 거부", description = "수혈 요청을 거부합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionRequestSummary>> rejectRequest(
            @Parameter(description = "수혈 요청 ID") @PathVariable Long id,
            @Parameter(description = "거부 사유") @RequestParam String rejectionReason) {
        TransfusionRequest request = transfusionFacade.rejectRequest(id, rejectionReason);
        return ok(TransfusionResponses.TransfusionRequestSummary.of(request));
    }

    @PostMapping("/records")
    @Operation(summary = "수혈 기록 등록", description = "수혈 기록을 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionRecordSummary>> createRecord(
            @Valid @RequestBody TransfusionRequests.CreateTransfusionRecordRequest request) {
        TransfusionRecord record = transfusionFacade.createRecord(request);
        return created(TransfusionResponses.TransfusionRecordSummary.of(record));
    }

    @PostMapping("/records/{id}/complete")
    @Operation(summary = "수혈 완료", description = "수혈을 완료 처리합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionRecordSummary>> completeRecord(
            @Parameter(description = "수혈 기록 ID") @PathVariable Long id) {
        TransfusionRecord record = transfusionFacade.completeRecord(id);
        return ok(TransfusionResponses.TransfusionRecordSummary.of(record));
    }

    @PostMapping("/reactions")
    @Operation(summary = "수혈 반응 등록", description = "수혈 반응을 등록합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<TransfusionResponses.TransfusionReactionSummary>> addReaction(
            @Valid @RequestBody TransfusionRequests.CreateTransfusionReactionRequest request) {
        TransfusionReaction reaction = transfusionFacade.addReaction(request);
        return created(TransfusionResponses.TransfusionReactionSummary.of(reaction));
    }

    @GetMapping("/records/{recordId}/reactions")
    @Operation(summary = "수혈 반응 조회", description = "수혈 기록의 반응 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<TransfusionResponses.TransfusionReactionSummary>>> getReactions(
            @Parameter(description = "수혈 기록 ID") @PathVariable Long recordId) {
        List<TransfusionReaction> reactions = transfusionFacade.getReactionsByRecord(recordId);
        List<TransfusionResponses.TransfusionReactionSummary> summaries = reactions.stream()
                .map(TransfusionResponses.TransfusionReactionSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }
}

