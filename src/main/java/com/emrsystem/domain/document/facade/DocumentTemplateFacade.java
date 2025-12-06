package com.emrsystem.domain.document.facade;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.mapper.DocumentTemplateMapper;
import com.emrsystem.domain.document.request.DocumentTemplateRequests;
import com.emrsystem.domain.document.response.DocumentTemplateResponses;
import com.emrsystem.domain.document.service.*;
import com.emrsystem.global.common.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DocumentTemplateFacade {

    private final DocumentTemplateService templateService;
    private final DocumentGenerationService generationService;
    private final TemplateVersionService versionService;
    private final TemplateTranslationService translationService;
    private final HospitalTemplateService hospitalTemplateService;
    private final TemplateAssetService assetService;
    private final DocumentTemplateMapper templateMapper;

    public DocumentTemplateResponses.TemplateDetail getTemplate(Long id) {
        return templateMapper.toDto(templateService.getTemplate(id));
    }

    public List<DocumentTemplateResponses.TemplateSummary> getTemplatesByDocumentType(String documentType) {
        return BaseMapper.fromList(
                templateService.getTemplatesByDocumentType(documentType),
                templateMapper::toSummary
        );
    }

    public List<DocumentTemplateResponses.TemplateSummary> getActiveTemplatesByDocumentType(String documentType) {
        return BaseMapper.fromList(
                templateService.getActiveTemplatesByDocumentType(documentType),
                templateMapper::toSummary
        );
    }

    public DocumentTemplateResponses.TemplateDetail getDefaultTemplate(String documentType) {
        return templateMapper.toDto(templateService.getDefaultTemplate(documentType));
    }

    public List<DocumentTemplateResponses.TemplateSummary> getAllActiveTemplates() {
        return BaseMapper.fromList(
                templateService.getAllActiveTemplates(),
                templateMapper::toSummary
        );
    }

    public DocumentTemplateResponses.TemplateDetail createTemplate(DocumentTemplateRequests.CreateTemplateRequest request) {
        DocumentTemplate template = templateService.createTemplate(request);
        return templateMapper.toDto(template);
    }

    public DocumentTemplateResponses.TemplateDetail updateTemplate(Long id, DocumentTemplateRequests.UpdateTemplateRequest request) {
        Long userId = request.getUserId() != null ? request.getUserId() : 1L; // 임시로 1L 사용
        DocumentTemplate template = templateService.updateTemplate(id, request, userId);
        return templateMapper.toDto(template);
    }

    public void activateTemplate(Long id) {
        templateService.activateTemplate(id);
    }

    public void deactivateTemplate(Long id) {
        templateService.deactivateTemplate(id);
    }

    public void setAsDefault(Long id) {
        templateService.setAsDefault(id);
    }

    public void deleteTemplate(Long id) {
        templateService.deleteTemplate(id);
    }

    public Resource generateDocument(DocumentTemplateRequests.GenerateDocumentRequest request) {
        return generationService.generateDocument(request);
    }

    public Resource generateDocumentByType(String documentType, Map<String, Object> data, String outputFormat) {
        return generationService.generateDocumentByType(documentType, data, outputFormat);
    }

    // 템플릿 미리보기
    public DocumentTemplateResponses.PreviewResponse previewTemplate(Long templateId, DocumentTemplateRequests.PreviewTemplateRequest request) {
        DocumentTemplate template = templateService.getTemplate(templateId);
        // 이미 조회한 템플릿을 사용하여 중복 조회 방지
        String htmlContent = generationService.previewTemplate(template, request.getData(), request.getLanguageCode());
        return new DocumentTemplateResponses.PreviewResponse(htmlContent, template.getTemplateName(), template.getDocumentType());
    }

    public DocumentTemplateResponses.PreviewResponse previewDefaultTemplate(String documentType, DocumentTemplateRequests.PreviewTemplateRequest request) {
        DocumentTemplate template = templateService.getDefaultTemplate(documentType);
        // 이미 조회한 템플릿을 사용하여 중복 조회 방지
        String htmlContent = generationService.previewTemplate(template, request.getData(), request.getLanguageCode());
        return new DocumentTemplateResponses.PreviewResponse(htmlContent, template.getTemplateName(), template.getDocumentType());
    }

    // 버전 관리
    public List<DocumentTemplateResponses.VersionSummary> getVersionsByTemplateId(Long templateId) {
        return BaseMapper.fromList(
                versionService.getVersionsByTemplate(templateId),
                DocumentTemplateResponses.VersionSummary::from
        );
    }

    public DocumentTemplateResponses.VersionSummary createVersion(Long templateId, DocumentTemplateRequests.CreateVersionRequest request) {
        com.emrsystem.domain.document.entity.TemplateVersion version = versionService.createVersion(templateId, request.getVersion(),
                request.getChangeDescription(), request.getCreatedByUserId());
        return DocumentTemplateResponses.VersionSummary.from(version);
    }

    public void restoreVersion(Long templateId, String version) {
        versionService.restoreVersion(templateId, version);
    }

    // 다국어 지원
    public List<DocumentTemplateResponses.TranslationSummary> getTranslationsByTemplateId(Long templateId) {
        return BaseMapper.fromList(
                translationService.getTranslationsByTemplate(templateId),
                DocumentTemplateResponses.TranslationSummary::from
        );
    }

    public DocumentTemplateResponses.TranslationSummary createTranslation(Long templateId, DocumentTemplateRequests.CreateTranslationRequest request) {
        com.emrsystem.domain.document.entity.TemplateTranslation translation = translationService.createTranslation(templateId, request.getLanguageCode(),
                request.getTemplateName(), request.getContent(), request.getDescription());
        return DocumentTemplateResponses.TranslationSummary.from(translation);
    }

    // 병원 템플릿 공유
    public List<DocumentTemplateResponses.HospitalTemplateSummary> getSharedTemplates() {
        return BaseMapper.fromList(
                hospitalTemplateService.getSharedTemplates(),
                DocumentTemplateResponses.HospitalTemplateSummary::from
        );
    }

    public DocumentTemplateResponses.HospitalTemplateSummary shareTemplate(Long templateId, DocumentTemplateRequests.ShareTemplateRequest request) {
        com.emrsystem.domain.document.entity.HospitalTemplate hospitalTemplate = hospitalTemplateService.assignTemplateToHospital(
                templateId, request.getHospitalId(), true, null);
        return DocumentTemplateResponses.HospitalTemplateSummary.from(hospitalTemplate);
    }

    // 에셋 관리
    public List<DocumentTemplateResponses.AssetSummary> getAssetsByTemplateId(Long templateId) {
        return BaseMapper.fromList(
                assetService.getAssetsByTemplate(templateId),
                DocumentTemplateResponses.AssetSummary::from
        );
    }

    public DocumentTemplateResponses.AssetSummary uploadAsset(Long templateId, MultipartFile file,
                                                              String assetType, String description) throws IOException {
        com.emrsystem.domain.document.entity.TemplateAsset asset = assetService.uploadAsset(
                templateId, null, assetType, file, description);
        return DocumentTemplateResponses.AssetSummary.from(asset);
    }

    public void deleteAsset(Long assetId) throws IOException {
        assetService.deleteAsset(assetId);
    }
}

