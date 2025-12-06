package com.emrsystem.domain.document.store;

import com.emrsystem.domain.document.entity.*;
import com.emrsystem.domain.document.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentTemplateStore {

    private final DocumentTemplateRepository documentTemplateRepository;
    private final TemplateVariableRepository templateVariableRepository;
    private final TemplateVersionRepository templateVersionRepository;
    private final TemplateTranslationRepository templateTranslationRepository;
    private final HospitalTemplateRepository hospitalTemplateRepository;
    private final TemplateAssetRepository templateAssetRepository;

    public DocumentTemplate saveTemplate(DocumentTemplate template) {
        return documentTemplateRepository.save(template);
    }

    public Optional<DocumentTemplate> findTemplateById(Long id) {
        return documentTemplateRepository.findById(id);
    }

    public List<DocumentTemplate> findTemplatesByDocumentType(String documentType) {
        return documentTemplateRepository.findByDocumentType(documentType);
    }

    public List<DocumentTemplate> findActiveTemplatesByDocumentType(String documentType) {
        return documentTemplateRepository.findByDocumentTypeAndActive(documentType, true);
    }

    public Optional<DocumentTemplate> findDefaultTemplateByDocumentType(String documentType) {
        return documentTemplateRepository.findByDocumentTypeAndIsDefault(documentType, true);
    }

    public List<DocumentTemplate> findAllActiveTemplates() {
        return documentTemplateRepository.findByActive(true);
    }

    public List<DocumentTemplate> findTemplatesByFormat(String format) {
        return documentTemplateRepository.findByFormat(format);
    }

    public boolean existsByTemplateName(String templateName) {
        return documentTemplateRepository.existsByTemplateName(templateName);
    }

    public void deleteTemplate(Long id) {
        documentTemplateRepository.deleteById(id);
    }

    public TemplateVariable saveVariable(TemplateVariable variable) {
        return templateVariableRepository.save(variable);
    }

    public List<TemplateVariable> findVariablesByTemplateId(Long templateId) {
        return templateVariableRepository.findByTemplate_TemplateId(templateId);
    }

    public void deleteVariablesByTemplateId(Long templateId) {
        templateVariableRepository.deleteByTemplate_TemplateId(templateId);
    }

    // Template Version operations
    public TemplateVersion saveTemplateVersion(TemplateVersion version) {
        return templateVersionRepository.save(version);
    }

    public List<TemplateVersion> findVersionsByTemplateId(Long templateId) {
        return templateVersionRepository.findByTemplate_TemplateIdOrderByCreatedAtDesc(templateId);
    }

    public Optional<TemplateVersion> findVersionByTemplateIdAndVersion(Long templateId, String version) {
        return templateVersionRepository.findByTemplate_TemplateIdAndVersion(templateId, version);
    }

    // Template Translation operations
    public TemplateTranslation saveTranslation(TemplateTranslation translation) {
        return templateTranslationRepository.save(translation);
    }

    public List<TemplateTranslation> findTranslationsByTemplateId(Long templateId) {
        return templateTranslationRepository.findByTemplate_TemplateId(templateId);
    }

    public Optional<TemplateTranslation> findTranslationByTemplateIdAndLanguage(Long templateId, String languageCode) {
        return templateTranslationRepository.findByTemplate_TemplateIdAndLanguageCode(templateId, languageCode);
    }

    public List<TemplateTranslation> findActiveTranslationsByLanguage(String languageCode) {
        return templateTranslationRepository.findByLanguageCodeAndActive(languageCode, true);
    }

    // Hospital Template operations
    public HospitalTemplate saveHospitalTemplate(HospitalTemplate hospitalTemplate) {
        return hospitalTemplateRepository.save(hospitalTemplate);
    }

    public List<HospitalTemplate> findHospitalTemplatesByHospitalId(Long hospitalId) {
        return hospitalTemplateRepository.findByHospital_HospitalIdAndActive(hospitalId, true);
    }

    public List<HospitalTemplate> findHospitalTemplatesByTemplateId(Long templateId) {
        return hospitalTemplateRepository.findByTemplate_TemplateId(templateId);
    }

    public Optional<HospitalTemplate> findHospitalTemplateByTemplateAndHospital(Long templateId, Long hospitalId) {
        return hospitalTemplateRepository.findByTemplate_TemplateIdAndHospital_HospitalId(templateId, hospitalId);
    }

    public List<HospitalTemplate> findSharedTemplates() {
        return hospitalTemplateRepository.findBySharedAndActive(true, true);
    }

    // Template Asset operations
    public TemplateAsset saveAsset(TemplateAsset asset) {
        return templateAssetRepository.save(asset);
    }

    public List<TemplateAsset> findAssetsByTemplateId(Long templateId) {
        return templateAssetRepository.findByTemplate_TemplateId(templateId);
    }

    public List<TemplateAsset> findActiveAssetsByTemplateId(Long templateId) {
        return templateAssetRepository.findByTemplate_TemplateIdAndActive(templateId, true);
    }

    public List<TemplateAsset> findAssetsByType(String assetType) {
        return templateAssetRepository.findByAssetTypeAndActive(assetType, true);
    }

    public List<TemplateAsset> findPublicAssets() {
        return templateAssetRepository.findByTemplateIsNullAndActive(true);
    }

    public Optional<TemplateAsset> findAssetById(Long assetId) {
        return templateAssetRepository.findById(assetId);
    }

    public void deleteAsset(Long assetId) {
        templateAssetRepository.deleteById(assetId);
    }
}

