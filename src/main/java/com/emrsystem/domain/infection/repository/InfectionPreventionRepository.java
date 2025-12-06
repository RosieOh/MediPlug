package com.emrsystem.domain.infection.repository;

import com.emrsystem.domain.infection.entity.InfectionPrevention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfectionPreventionRepository extends JpaRepository<InfectionPrevention, Long> {
    List<InfectionPrevention> findByInfectionCase_InfectionCaseId(Long infectionCaseId);
    List<InfectionPrevention> findByInfectionCase_InfectionCaseIdAndActiveTrue(Long infectionCaseId);
}

