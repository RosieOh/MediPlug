package com.emrsystem.domain.document.controller;

import com.emrsystem.domain.document.facade.DocumentTemplateFacade;
import com.emrsystem.domain.document.request.DocumentTemplateRequests;
import com.emrsystem.domain.document.response.DocumentTemplateResponses;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Document Template", description = "문서 템플릿 관리 API")
@RestController
@RequestMapping("/api/document-templates")
@RequiredArgsConstructor
public class DocumentTemplateController extends BaseController {

    private final DocumentTemplateFacade templateFacade;

    @GetMapping
    @Operation(summary = "활성 템플릿 목록 조회", description = "모든 활성 템플릿 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<DocumentTemplateResponses.TemplateSummary>>> getAllActiveTemplates() {
        return ok(templateFacade.getAllActiveTemplates());
    }

    @GetMapping("/type/{documentType}")
    @Operation(summary = "문서 타입별 템플릿 조회", description = "특정 문서 타입의 템플릿 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<DocumentTemplateResponses.TemplateSummary>>> getTemplatesByType(
            @Parameter(description = "문서 타입") @PathVariable String documentType) {
        return ok(templateFacade.getActiveTemplatesByDocumentType(documentType));
    }

    @GetMapping("/type/{documentType}/default")
    @Operation(summary = "기본 템플릿 조회", description = "특정 문서 타입의 기본 템플릿을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.TemplateDetail>> getDefaultTemplate(
            @Parameter(description = "문서 타입") @PathVariable String documentType) {
        return ok(templateFacade.getDefaultTemplate(documentType));
    }

    @GetMapping("/{id}")
    @Operation(summary = "템플릿 상세 조회", description = "템플릿 상세 정보를 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.TemplateDetail>> getTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        return ok(templateFacade.getTemplate(id));
    }

    @PostMapping
    @Operation(summary = "템플릿 생성", description = "새로운 문서 템플릿을 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.TemplateDetail>> createTemplate(
            @Valid @RequestBody DocumentTemplateRequests.CreateTemplateRequest request) {
        return created(templateFacade.createTemplate(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "템플릿 수정", description = "기존 템플릿을 수정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.TemplateDetail>> updateTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Valid @RequestBody DocumentTemplateRequests.UpdateTemplateRequest request) {
        return ok(templateFacade.updateTemplate(id, request));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "템플릿 활성화", description = "템플릿을 활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<String>> activateTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        templateFacade.activateTemplate(id);
        return okMessage("템플릿이 활성화되었습니다.");
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "템플릿 비활성화", description = "템플릿을 비활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<String>> deactivateTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        templateFacade.deactivateTemplate(id);
        return okMessage("템플릿이 비활성화되었습니다.");
    }

    @PostMapping("/{id}/set-default")
    @Operation(summary = "기본 템플릿 설정", description = "템플릿을 기본 템플릿으로 설정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<String>> setAsDefault(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        templateFacade.setAsDefault(id);
        return okMessage("기본 템플릿으로 설정되었습니다.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "템플릿 삭제", description = "템플릿을 삭제합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<String>> deleteTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        templateFacade.deleteTemplate(id);
        return okMessage("템플릿이 삭제되었습니다.");
    }

    @PostMapping("/generate")
    @Operation(summary = "템플릿 기반 문서 생성", description = "템플릿을 사용하여 문서를 생성합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<Resource> generateDocument(
            @Valid @RequestBody DocumentTemplateRequests.GenerateDocumentRequest request) {
        Resource resource = templateFacade.generateDocument(request);

        String format = request.getOutputFormat() != null
                ? request.getOutputFormat().toUpperCase()
                : "PDF";
        
        String contentType = switch (format) {
            case "PDF" -> MediaType.APPLICATION_PDF_VALUE;
            case "WORD" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "EXCEL" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "HTML" -> MediaType.TEXT_HTML_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };

        String extension = format.toLowerCase();
        String filename = "document_" + System.currentTimeMillis() + "." + extension;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping("/generate/{documentType}")
    @Operation(summary = "기본 템플릿으로 문서 생성", description = "문서 타입의 기본 템플릿을 사용하여 문서를 생성합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<Resource> generateDocumentByType(
            @Parameter(description = "문서 타입") @PathVariable String documentType,
            @RequestBody Map<String, Object> data,
            @Parameter(description = "출력 포맷 (PDF, WORD, EXCEL, HTML)") @RequestParam(required = false) String outputFormat) {
        Resource resource = templateFacade.generateDocumentByType(documentType, data, outputFormat);

        String format = outputFormat != null ? outputFormat.toUpperCase() : "PDF";
        String contentType = switch (format) {
            case "PDF" -> MediaType.APPLICATION_PDF_VALUE;
            case "WORD" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "EXCEL" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "HTML" -> MediaType.TEXT_HTML_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };

        String extension = format.toLowerCase();
        String filename = documentType.toLowerCase() + "_" + System.currentTimeMillis() + "." + extension;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    // ========== 템플릿 버전 관리 ==========
    @GetMapping("/{id}/versions")
    @Operation(summary = "템플릿 버전 목록 조회", description = "템플릿의 모든 버전 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<DocumentTemplateResponses.VersionSummary>>> getVersions(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        return ok(templateFacade.getVersionsByTemplateId(id));
    }

    @PostMapping("/{id}/versions")
    @Operation(summary = "템플릿 버전 생성", description = "템플릿의 새 버전을 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.VersionSummary>> createVersion(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Valid @RequestBody DocumentTemplateRequests.CreateVersionRequest request) {
        return created(templateFacade.createVersion(id, request));
    }

    @PostMapping("/{id}/versions/{version}/restore")
    @Operation(summary = "템플릿 버전 복원", description = "특정 버전으로 템플릿을 복원합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<String>> restoreVersion(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Parameter(description = "버전") @PathVariable String version) {
        templateFacade.restoreVersion(id, version);
        return okMessage("템플릿이 버전 " + version + "으로 복원되었습니다.");
    }

    // ========== 템플릿 미리보기 ==========
    @PostMapping("/{id}/preview")
    @Operation(summary = "템플릿 미리보기", description = "템플릿을 미리보기합니다 (HTML 반환).")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.PreviewResponse>> previewTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Valid @RequestBody DocumentTemplateRequests.PreviewTemplateRequest request) {
        return ok(templateFacade.previewTemplate(id, request));
    }

    @PostMapping("/type/{documentType}/preview")
    @Operation(summary = "기본 템플릿 미리보기", description = "기본 템플릿을 미리보기합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.PreviewResponse>> previewDefaultTemplate(
            @Parameter(description = "문서 타입") @PathVariable String documentType,
            @Valid @RequestBody DocumentTemplateRequests.PreviewTemplateRequest request) {
        return ok(templateFacade.previewDefaultTemplate(documentType, request));
    }

    // ========== 다국어 지원 ==========
    @GetMapping("/{id}/translations")
    @Operation(summary = "템플릿 번역 목록 조회", description = "템플릿의 모든 번역 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<DocumentTemplateResponses.TranslationSummary>>> getTranslations(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        return ok(templateFacade.getTranslationsByTemplateId(id));
    }

    @PostMapping("/{id}/translations")
    @Operation(summary = "템플릿 번역 생성", description = "템플릿의 새 번역을 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.TranslationSummary>> createTranslation(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Valid @RequestBody DocumentTemplateRequests.CreateTranslationRequest request) {
        return created(templateFacade.createTranslation(id, request));
    }

    // ========== 템플릿 공유 ==========
    @GetMapping("/shared")
    @Operation(summary = "공유 템플릿 목록 조회", description = "공유된 템플릿 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<DocumentTemplateResponses.HospitalTemplateSummary>>> getSharedTemplates() {
        return ok(templateFacade.getSharedTemplates());
    }

    @PostMapping("/{id}/share")
    @Operation(summary = "템플릿 공유", description = "템플릿을 다른 병원과 공유합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.HospitalTemplateSummary>> shareTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Valid @RequestBody DocumentTemplateRequests.ShareTemplateRequest request) {
        return created(templateFacade.shareTemplate(id, request));
    }

    // ========== 템플릿 에셋 관리 ==========
    @GetMapping("/{id}/assets")
    @Operation(summary = "템플릿 에셋 목록 조회", description = "템플릿의 모든 에셋 목록을 조회합니다.")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<ApiResponse<List<DocumentTemplateResponses.AssetSummary>>> getAssets(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        return ok(templateFacade.getAssetsByTemplateId(id));
    }

    @PostMapping("/{id}/assets")
    @Operation(summary = "템플릿 에셋 업로드", description = "템플릿에 에셋(이미지, 로고 등)을 업로드합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<DocumentTemplateResponses.AssetSummary>> uploadAsset(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("assetType") String assetType,
            @RequestParam(value = "description", required = false) String description) throws java.io.IOException {
        return created(templateFacade.uploadAsset(id, file, assetType, description));
    }

    @DeleteMapping("/assets/{assetId}")
    @Operation(summary = "템플릿 에셋 삭제", description = "템플릿 에셋을 삭제합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<String>> deleteAsset(
            @Parameter(description = "에셋 ID") @PathVariable Long assetId) throws java.io.IOException {
        templateFacade.deleteAsset(assetId);
        return okMessage("에셋이 삭제되었습니다.");
    }
}

