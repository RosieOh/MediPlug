package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.DiagnosisCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisCodeRepository extends JpaRepository<DiagnosisCode, Long> {
    Optional<DiagnosisCode> findByCode(String code);
    Page<DiagnosisCode> findByActiveTrue(Pageable pageable);
    Page<DiagnosisCode> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name, Pageable pageable);
    List<DiagnosisCode> findByCategoryAndActiveTrue(String category);
    boolean existsByCode(String code);
}

