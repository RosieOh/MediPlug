package com.emrsystem.domain.schedule.repository;

import com.emrsystem.domain.schedule.entity.StaffSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Long> {
    List<StaffSchedule> findByDoctor_DoctorId(Long doctorId);
    List<StaffSchedule> findByScheduleDate(LocalDate scheduleDate);
    List<StaffSchedule> findByScheduleDateBetween(LocalDate startDate, LocalDate endDate);
    List<StaffSchedule> findByDoctor_DoctorIdAndScheduleDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);
    List<StaffSchedule> findByStaffTypeAndScheduleDateBetween(String staffType, LocalDate startDate, LocalDate endDate);
    List<StaffSchedule> findByShiftTypeAndScheduleDate(String shiftType, LocalDate scheduleDate);
    List<StaffSchedule> findByStatus(String status);
}

