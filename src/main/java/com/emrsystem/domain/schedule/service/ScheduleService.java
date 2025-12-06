package com.emrsystem.domain.schedule.service;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.schedule.entity.Shift;
import com.emrsystem.domain.schedule.entity.StaffSchedule;
import com.emrsystem.domain.schedule.request.ScheduleRequests;
import com.emrsystem.domain.schedule.response.ScheduleResponses;
import com.emrsystem.domain.schedule.store.ScheduleStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleStore scheduleStore;
    private final DoctorStore doctorStore;

    // StaffSchedule operations
    public List<StaffSchedule> getSchedulesByDoctor(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return scheduleStore.findStaffSchedulesByDoctorAndDateRange(doctorId, startDate, endDate);
    }

    public List<StaffSchedule> getSchedulesByDate(LocalDate date) {
        return scheduleStore.findStaffSchedulesByDate(date);
    }

    public List<StaffSchedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleStore.findStaffSchedulesByDateRange(startDate, endDate);
    }

    public List<StaffSchedule> getSchedulesByStaffType(String staffType, LocalDate startDate, LocalDate endDate) {
        return scheduleStore.findStaffSchedulesByStaffTypeAndDateRange(staffType, startDate, endDate);
    }

    @Transactional
    public StaffSchedule createDoctorSchedule(ScheduleRequests.CreateDoctorScheduleRequest request) {
        Doctor doctor = doctorStore.findById(request.getDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        // 스케줄 충돌 체크
        checkScheduleConflict(doctor.getDoctorId(), request.getScheduleDate(), request.getStartTime(), request.getEndTime());

        StaffSchedule schedule = StaffSchedule.createForDoctor(
                doctor,
                request.getScheduleDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getShiftType(),
                request.getDepartment(),
                request.getLocation()
        );

        return scheduleStore.saveStaffSchedule(schedule);
    }

    @Transactional
    public StaffSchedule createStaffSchedule(ScheduleRequests.CreateStaffScheduleRequest request) {
        StaffSchedule schedule = StaffSchedule.createForStaff(
                request.getStaffName(),
                request.getStaffType(),
                request.getScheduleDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getShiftType(),
                request.getDepartment(),
                request.getLocation()
        );

        return scheduleStore.saveStaffSchedule(schedule);
    }

    @Transactional
    public StaffSchedule updateSchedule(Long id, ScheduleRequests.UpdateScheduleRequest request) {
        StaffSchedule schedule = scheduleStore.findStaffScheduleById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "스케줄을 찾을 수 없습니다."));

        if (schedule.getDoctor() != null) {
            // 스케줄 충돌 체크
            checkScheduleConflict(schedule.getDoctor().getDoctorId(), schedule.getScheduleDate(),
                    request.getStartTime(), request.getEndTime());
        }

        schedule.update(
                request.getStartTime(),
                request.getEndTime(),
                request.getShiftType(),
                request.getDepartment(),
                request.getLocation(),
                request.getNotes()
        );

        return scheduleStore.saveStaffSchedule(schedule);
    }

    @Transactional
    public StaffSchedule confirmSchedule(Long id) {
        StaffSchedule schedule = scheduleStore.findStaffScheduleById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "스케줄을 찾을 수 없습니다."));
        schedule.confirm();
        return scheduleStore.saveStaffSchedule(schedule);
    }

    @Transactional
    public StaffSchedule cancelSchedule(Long id) {
        StaffSchedule schedule = scheduleStore.findStaffScheduleById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "스케줄을 찾을 수 없습니다."));
        schedule.cancel();
        return scheduleStore.saveStaffSchedule(schedule);
    }

    @Transactional
    public StaffSchedule completeSchedule(Long id) {
        StaffSchedule schedule = scheduleStore.findStaffScheduleById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "스케줄을 찾을 수 없습니다."));
        schedule.complete();
        return scheduleStore.saveStaffSchedule(schedule);
    }

    // Shift operations
    public List<Shift> getActiveShifts() {
        return scheduleStore.findShiftsByStatus("ACTIVE");
    }

    public Shift getShift(Long id) {
        return scheduleStore.findShiftById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "교대 근무를 찾을 수 없습니다."));
    }

    @Transactional
    public Shift createShift(ScheduleRequests.CreateShiftRequest request) {
        if (scheduleStore.existsShiftByShiftType(request.getShiftType())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "이미 존재하는 교대 근무 유형입니다.");
        }

        Shift shift = Shift.create(
                request.getShiftType(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDescription()
        );

        return scheduleStore.saveShift(shift);
    }

    @Transactional
    public Shift updateShift(Long id, ScheduleRequests.UpdateShiftRequest request) {
        Shift shift = getShift(id);
        shift.update(request.getStartTime(), request.getEndTime(), request.getDescription());
        return scheduleStore.saveShift(shift);
    }

    // Statistics
    public ScheduleResponses.ScheduleStatistics getScheduleStatistics(LocalDate startDate, LocalDate endDate) {
        List<StaffSchedule> schedules = scheduleStore.findStaffSchedulesByDateRange(startDate, endDate);

        long totalSchedules = schedules.size();
        long confirmedSchedules = schedules.stream()
                .filter(s -> "CONFIRMED".equals(s.getStatus()))
                .count();
        long cancelledSchedules = schedules.stream()
                .filter(s -> "CANCELLED".equals(s.getStatus()))
                .count();
        long completedSchedules = schedules.stream()
                .filter(s -> "COMPLETED".equals(s.getStatus()))
                .count();

        return ScheduleResponses.ScheduleStatistics.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalSchedules(totalSchedules)
                .confirmedSchedules(confirmedSchedules)
                .cancelledSchedules(cancelledSchedules)
                .completedSchedules(completedSchedules)
                .build();
    }

    private void checkScheduleConflict(Long doctorId, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) {
        List<StaffSchedule> existingSchedules = scheduleStore.findStaffSchedulesByDoctorAndDateRange(
                doctorId, scheduleDate, scheduleDate);

        for (StaffSchedule existing : existingSchedules) {
            if (!"CANCELLED".equals(existing.getStatus())) {
                if (isTimeOverlapping(startTime, endTime, existing.getStartTime(), existing.getEndTime())) {
                    throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                            "해당 시간대에 이미 스케줄이 있습니다.");
                }
            }
        }
    }

    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}

