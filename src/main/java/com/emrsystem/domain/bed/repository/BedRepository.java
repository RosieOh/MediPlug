package com.emrsystem.domain.bed.repository;

import com.emrsystem.domain.bed.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByRoom_RoomId(Long roomId);
    List<Bed> findByStatus(String status);
    List<Bed> findByBedType(String bedType);
    List<Bed> findByStatusAndBedType(String status, String bedType);
    List<Bed> findByStatusAndRoom_RoomId(String status, Long roomId);
    boolean existsByRoom_RoomIdAndBedNumber(Long roomId, String bedNumber);
    
    // Statistics queries
    long countByStatus(String status);
}
