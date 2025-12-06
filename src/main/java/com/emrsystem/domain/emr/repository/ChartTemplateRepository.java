package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.ChartTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChartTemplateRepository extends JpaRepository<ChartTemplate, Long> {
    Optional<ChartTemplate> findByCode(String code);
    boolean existsByCode(String code);
}