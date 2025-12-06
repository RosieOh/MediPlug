package com.emrsystem.domain.schedule.facade;

import com.emrsystem.domain.schedule.entity.Shift;
import com.emrsystem.domain.schedule.entity.StaffSchedule;
import com.emrsystem.domain.schedule.request.ScheduleRequests;
import com.emrsystem.domain.schedule.response.ScheduleResponses;
import com.emrsystem.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {

    private final ScheduleService scheduleService;

    public List<StaffSchedule> getSchedulesByDoctor(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return scheduleService.getSchedulesByDoctor(doctorId, startDate, endDate);
    }

    public List<StaffSchedule> getSchedulesByDate(LocalDate date) {
        return scheduleService.getSchedulesByDate(date);
    }

    public List<StaffSchedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleService.getSchedulesByDateRange(startDate, endDate);
    }

    public List<StaffSchedule> getSchedulesByStaffType(String staffType, LocalDate startDate, LocalDate endDate) {
        return scheduleService.getSchedulesByStaffType(staffType, startDate, endDate);
    }

    public StaffSchedule createDoctorSchedule(ScheduleRequests.CreateDoctorScheduleRequest request) {
        return scheduleService.createDoctorSchedule(request);
    }

    public StaffSchedule createStaffSchedule(ScheduleRequests.CreateStaffScheduleRequest request) {
        return scheduleService.createStaffSchedule(request);
    }

    public StaffSchedule updateSchedule(Long id, ScheduleRequests.UpdateScheduleRequest request) {
        return scheduleService.updateSchedule(id, request);
    }

    public StaffSchedule confirmSchedule(Long id) {
        return scheduleService.confirmSchedule(id);
    }

    public StaffSchedule cancelSchedule(Long id) {
        return scheduleService.cancelSchedule(id);
    }

    public StaffSchedule completeSchedule(Long id) {
        return scheduleService.completeSchedule(id);
    }

    public List<Shift> getActiveShifts() {
        return scheduleService.getActiveShifts();
    }

    public Shift getShift(Long id) {
        return scheduleService.getShift(id);
    }

    public Shift createShift(ScheduleRequests.CreateShiftRequest request) {
        return scheduleService.createShift(request);
    }

    public Shift updateShift(Long id, ScheduleRequests.UpdateShiftRequest request) {
        return scheduleService.updateShift(id, request);
    }

    public ScheduleResponses.ScheduleStatistics getScheduleStatistics(LocalDate startDate, LocalDate endDate) {
        return scheduleService.getScheduleStatistics(startDate, endDate);
    }
}

