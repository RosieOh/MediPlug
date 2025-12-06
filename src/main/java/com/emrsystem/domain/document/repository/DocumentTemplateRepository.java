package com.emrsystem.domain.document.repository;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long> {
    List<DocumentTemplate> findByDocumentType(String documentType);
    List<DocumentTemplate> findByDocumentTypeAndActive(String documentType, boolean active);
    Optional<DocumentTemplate> findByDocumentTypeAndIsDefault(String documentType, boolean isDefault);
    List<DocumentTemplate> findByActive(boolean active);
    List<DocumentTemplate> findByFormat(String format);
    boolean existsByTemplateName(String templateName);
}

