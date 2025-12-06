package com.emrsystem.domain.pharmacy.repository;

import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, Long> {
    @Query("SELECT di FROM DrugInteraction di WHERE " +
           "((di.drug1.drugMasterId = :drug1Id AND di.drug2.drugMasterId = :drug2Id) OR " +
           "(di.drug1.drugMasterId = :drug2Id AND di.drug2.drugMasterId = :drug1Id)) " +
           "AND di.active = true")
    Optional<DrugInteraction> findInteractionBetweenDrugs(@Param("drug1Id") Long drug1Id, @Param("drug2Id") Long drug2Id);

    @Query("SELECT di FROM DrugInteraction di WHERE " +
           "(di.drug1.drugMasterId = :drugId OR di.drug2.drugMasterId = :drugId) " +
           "AND di.active = true")
    List<DrugInteraction> findByDrugId(@Param("drugId") Long drugId);

    @Query("SELECT di FROM DrugInteraction di WHERE " +
           "((di.drug1.drugMasterId = :drug1Id AND di.drug2.drugMasterId = :drug2Id) OR " +
           "(di.drug1.drugMasterId = :drug2Id AND di.drug2.drugMasterId = :drug1Id)) " +
           "AND di.active = true")
    List<DrugInteraction> findInteractionsBetweenDrugs(@Param("drug1Id") Long drug1Id, @Param("drug2Id") Long drug2Id);

    List<DrugInteraction> findBySeverityAndActiveTrue(String severity);
}

