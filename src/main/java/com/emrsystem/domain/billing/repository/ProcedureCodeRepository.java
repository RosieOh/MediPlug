package com.emrsystem.domain.billing.repository;

import com.emrsystem.domain.billing.entity.ProcedureCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcedureCodeRepository extends JpaRepository<ProcedureCode, Long> {
    Optional<ProcedureCode> findByCode(String code);
    boolean existsByCode(String code);
}
