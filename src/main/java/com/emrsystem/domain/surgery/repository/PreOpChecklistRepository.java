package com.emrsystem.domain.surgery.repository;

import com.emrsystem.domain.surgery.entity.PreOpChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreOpChecklistRepository extends JpaRepository<PreOpChecklist, Long> {
    List<PreOpChecklist> findBySurgery_SurgeryId(Long surgeryId);
    List<PreOpChecklist> findBySurgery_SurgeryIdAndChecked(Long surgeryId, boolean checked);
}

