package com.emrsystem.domain.transfusion.repository;

import com.emrsystem.domain.transfusion.entity.TransfusionReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransfusionReactionRepository extends JpaRepository<TransfusionReaction, Long> {
    List<TransfusionReaction> findByTransfusionRecord_TransfusionRecordId(Long transfusionRecordId);
}

