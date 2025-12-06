package com.emrsystem.domain.schedule.store;

import com.emrsystem.domain.schedule.entity.Shift;
import com.emrsystem.domain.schedule.entity.StaffSchedule;
import com.emrsystem.domain.schedule.repository.ShiftRepository;
import com.emrsystem.domain.schedule.repository.StaffScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduleStore {

    private final StaffScheduleRepository staffScheduleRepository;
    private final ShiftRepository shiftRepository;

    // StaffSchedule operations
    public StaffSchedule saveStaffSchedule(StaffSchedule schedule) {
        return staffScheduleRepository.save(schedule);
    }

    public Optional<StaffSchedule> findStaffScheduleById(Long id) {
        return staffScheduleRepository.findById(id);
    }

    public List<StaffSchedule> findStaffSchedulesByDoctor(Long doctorId) {
        return staffScheduleRepository.findByDoctor_DoctorId(doctorId);
    }

    public List<StaffSchedule> findStaffSchedulesByDate(LocalDate date) {
        return staffScheduleRepository.findByScheduleDate(date);
    }

    public List<StaffSchedule> findStaffSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return staffScheduleRepository.findByScheduleDateBetween(startDate, endDate);
    }

    public List<StaffSchedule> findStaffSchedulesByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return staffScheduleRepository.findByDoctor_DoctorIdAndScheduleDateBetween(doctorId, startDate, endDate);
    }

    public List<StaffSchedule> findStaffSchedulesByStaffTypeAndDateRange(String staffType, LocalDate startDate, LocalDate endDate) {
        return staffScheduleRepository.findByStaffTypeAndScheduleDateBetween(staffType, startDate, endDate);
    }

    public List<StaffSchedule> findStaffSchedulesByShiftTypeAndDate(String shiftType, LocalDate date) {
        return staffScheduleRepository.findByShiftTypeAndScheduleDate(shiftType, date);
    }

    public List<StaffSchedule> findStaffSchedulesByStatus(String status) {
        return staffScheduleRepository.findByStatus(status);
    }

    // Shift operations
    public Shift saveShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    public Optional<Shift> findShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    public List<Shift> findShiftsByStatus(String status) {
        return shiftRepository.findByStatus(status);
    }

    public Optional<Shift> findShiftByShiftType(String shiftType) {
        return shiftRepository.findByShiftType(shiftType);
    }

    public boolean existsShiftByShiftType(String shiftType) {
        return shiftRepository.existsByShiftType(shiftType);
    }
}

