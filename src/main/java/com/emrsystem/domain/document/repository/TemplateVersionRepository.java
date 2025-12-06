package com.emrsystem.domain.document.repository;

import com.emrsystem.domain.document.entity.TemplateVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateVersionRepository extends JpaRepository<TemplateVersion, Long> {
    List<TemplateVersion> findByTemplate_TemplateIdOrderByCreatedAtDesc(Long templateId);
    Optional<TemplateVersion> findByTemplate_TemplateIdAndVersion(Long templateId, String version);
    List<TemplateVersion> findByTemplate_TemplateId(Long templateId);
}
