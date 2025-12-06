package com.emrsystem.domain.document.repository;

import com.emrsystem.domain.document.entity.TemplateVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateVariableRepository extends JpaRepository<TemplateVariable, Long> {
    List<TemplateVariable> findByTemplate_TemplateId(Long templateId);
    void deleteByTemplate_TemplateId(Long templateId);
}

