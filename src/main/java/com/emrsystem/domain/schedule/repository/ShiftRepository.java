package com.emrsystem.domain.schedule.repository;

import com.emrsystem.domain.schedule.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByStatus(String status);
    Optional<Shift> findByShiftType(String shiftType);
    boolean existsByShiftType(String shiftType);
}

