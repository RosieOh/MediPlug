package com.emrsystem.domain.surgery.repository;

import com.emrsystem.domain.surgery.entity.SurgeryTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurgeryTeamRepository extends JpaRepository<SurgeryTeam, Long> {
    List<SurgeryTeam> findBySurgery_SurgeryId(Long surgeryId);
    List<SurgeryTeam> findByDoctor_DoctorId(Long doctorId);
}

