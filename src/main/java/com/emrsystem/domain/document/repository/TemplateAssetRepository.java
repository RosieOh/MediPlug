package com.emrsystem.domain.document.repository;

import com.emrsystem.domain.document.entity.TemplateAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateAssetRepository extends JpaRepository<TemplateAsset, Long> {
    List<TemplateAsset> findByTemplate_TemplateId(Long templateId);
    List<TemplateAsset> findByTemplate_TemplateIdAndActive(Long templateId, boolean active);
    List<TemplateAsset> findByAssetTypeAndActive(String assetType, boolean active);
    List<TemplateAsset> findByTemplateIsNullAndActive(boolean active); // 공용 에셋
}
