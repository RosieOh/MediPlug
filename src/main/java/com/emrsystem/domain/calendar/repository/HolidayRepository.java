package com.emrsystem.domain.calendar.repository;

import com.emrsystem.domain.calendar.entity.Holiday;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    Optional<Holiday> findByDate(LocalDate date);
}


