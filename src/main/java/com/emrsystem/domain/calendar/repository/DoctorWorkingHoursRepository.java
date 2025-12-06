package com.emrsystem.domain.calendar.repository;

import com.emrsystem.domain.calendar.entity.DoctorWorkingHours;
import java.time.DayOfWeek;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorWorkingHoursRepository extends JpaRepository<DoctorWorkingHours, Long> {
    Optional<DoctorWorkingHours> findByDoctor_IdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}


