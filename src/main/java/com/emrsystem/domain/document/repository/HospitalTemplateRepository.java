package com.emrsystem.domain.document.repository;

import com.emrsystem.domain.document.entity.HospitalTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalTemplateRepository extends JpaRepository<HospitalTemplate, Long> {
    List<HospitalTemplate> findByTemplate_TemplateId(Long templateId);
    List<HospitalTemplate> findByHospital_HospitalId(Long hospitalId);
    List<HospitalTemplate> findByHospital_HospitalIdAndActive(Long hospitalId, boolean active);
    List<HospitalTemplate> findBySharedAndActive(boolean shared, boolean active);
    Optional<HospitalTemplate> findByTemplate_TemplateIdAndHospital_HospitalId(Long templateId, Long hospitalId);
}
