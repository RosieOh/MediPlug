package com.emrsystem.domain.schedule.response;

import com.emrsystem.domain.schedule.entity.Shift;
import com.emrsystem.domain.schedule.entity.StaffSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponses {

    @Getter
    @NoArgsConstructor
    public static class StaffScheduleSummary {
        private Long scheduleId;
        private Long doctorId;
        private String doctorName;
        private String staffType;
        private String staffName;
        private LocalDate scheduleDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String shiftType;
        private String department;
        private String location;
        private String status;
        private String notes;

        public static StaffScheduleSummary of(StaffSchedule schedule) {
            StaffScheduleSummary summary = new StaffScheduleSummary();
            summary.scheduleId = schedule.getId();
            if (schedule.getDoctor() != null) {
                summary.doctorId = schedule.getDoctor().getDoctorId();
                summary.doctorName = schedule.getDoctor().getName();
            }
            summary.staffType = schedule.getStaffType();
            summary.staffName = schedule.getStaffName();
            summary.scheduleDate = schedule.getScheduleDate();
            summary.startTime = schedule.getStartTime();
            summary.endTime = schedule.getEndTime();
            summary.shiftType = schedule.getShiftType();
            summary.department = schedule.getDepartment();
            summary.location = schedule.getLocation();
            summary.status = schedule.getStatus();
            summary.notes = schedule.getNotes();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ShiftSummary {
        private Long shiftId;
        private String shiftType;
        private LocalTime startTime;
        private LocalTime endTime;
        private int durationHours;
        private String description;
        private String status;

        public static ShiftSummary of(Shift shift) {
            ShiftSummary summary = new ShiftSummary();
            summary.shiftId = shift.getId();
            summary.shiftType = shift.getShiftType();
            summary.startTime = shift.getStartTime();
            summary.endTime = shift.getEndTime();
            summary.durationHours = shift.getDurationHours();
            summary.description = shift.getDescription();
            summary.status = shift.getStatus();
            return summary;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleStatistics {
        private LocalDate startDate;
        private LocalDate endDate;
        private long totalSchedules;
        private long confirmedSchedules;
        private long cancelledSchedules;
        private long completedSchedules;
    }
}

