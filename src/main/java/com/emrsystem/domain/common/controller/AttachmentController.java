package com.emrsystem.domain.common.controller;

import com.emrsystem.domain.common.request.AttachmentRequests;
import com.emrsystem.domain.common.response.AttachmentResponses;
import com.emrsystem.domain.common.service.AttachmentService;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Attachment", description = "파일 첨부 관리 API")
@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController extends BaseController {

    private final AttachmentService attachmentService;

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "엔티티별 첨부파일 조회", description = "특정 엔티티의 첨부파일 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<AttachmentResponses.AttachmentSummary>>> getByEntity(
            @Parameter(description = "엔티티 타입") @PathVariable String entityType,
            @Parameter(description = "엔티티 ID") @PathVariable Long entityId) {
        List<AttachmentResponses.AttachmentSummary> attachments = attachmentService.getByEntity(entityType, entityId).stream()
                .map(AttachmentResponses.AttachmentSummary::from)
                .collect(Collectors.toList());
        return ok(attachments);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 첨부파일 조회", description = "특정 카테고리의 첨부파일 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<AttachmentResponses.AttachmentSummary>>> getByCategory(
            @Parameter(description = "카테고리") @PathVariable String category) {
        List<AttachmentResponses.AttachmentSummary> attachments = attachmentService.getByCategory(category).stream()
                .map(AttachmentResponses.AttachmentSummary::from)
                .collect(Collectors.toList());
        return ok(attachments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "첨부파일 상세 조회", description = "특정 첨부파일의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<AttachmentResponses.AttachmentSummary>> get(
            @Parameter(description = "첨부파일 ID") @PathVariable Long id) {
        return ok(AttachmentResponses.AttachmentSummary.from(attachmentService.get(id)));
    }

    @PostMapping
    @Operation(summary = "첨부파일 등록", description = "새로운 첨부파일을 등록합니다.")
    public ResponseEntity<ApiResponse<AttachmentResponses.AttachmentSummary>> create(
            @Valid @RequestBody AttachmentRequests.CreateAttachmentRequest request) {
        return created(AttachmentResponses.AttachmentSummary.from(attachmentService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "첨부파일 수정", description = "기존 첨부파일을 수정합니다.")
    public ResponseEntity<ApiResponse<AttachmentResponses.AttachmentSummary>> update(
            @Parameter(description = "첨부파일 ID") @PathVariable Long id,
            @Valid @RequestBody AttachmentRequests.UpdateAttachmentRequest request) {
        return ok(AttachmentResponses.AttachmentSummary.from(attachmentService.update(id, request)));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "첨부파일 비활성화", description = "첨부파일을 비활성화합니다.")
    public ResponseEntity<ApiResponse<String>> deactivate(
            @Parameter(description = "첨부파일 ID") @PathVariable Long id) {
        attachmentService.deactivate(id);
        return okMessage("deactivated");
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "첨부파일 활성화", description = "첨부파일을 활성화합니다.")
    public ResponseEntity<ApiResponse<String>> activate(
            @Parameter(description = "첨부파일 ID") @PathVariable Long id) {
        attachmentService.activate(id);
        return okMessage("activated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "첨부파일 삭제", description = "첨부파일을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> delete(
            @Parameter(description = "첨부파일 ID") @PathVariable Long id) {
        attachmentService.delete(id);
        return okMessage("deleted");
    }
}

