package com.emrsystem.domain.document.repository;

import com.emrsystem.domain.document.entity.TemplateTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateTranslationRepository extends JpaRepository<TemplateTranslation, Long> {
    List<TemplateTranslation> findByTemplate_TemplateId(Long templateId);
    List<TemplateTranslation> findByTemplate_TemplateIdAndActive(Long templateId, boolean active);
    Optional<TemplateTranslation> findByTemplate_TemplateIdAndLanguageCode(Long templateId, String languageCode);
    List<TemplateTranslation> findByLanguageCodeAndActive(String languageCode, boolean active);
}
