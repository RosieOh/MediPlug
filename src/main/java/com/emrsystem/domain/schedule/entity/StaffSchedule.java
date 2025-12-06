package com.emrsystem.domain.schedule.entity;

import com.emrsystem.domain.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "staff_schedule")
@EntityListeners(AuditingEntityListener.class)
public class StaffSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // 의사 (의사 스케줄인 경우)

    @Column(nullable = false, length = 50)
    private String staffType; // DOCTOR, NURSE, OTHER

    @Column(length = 200)
    private String staffName; // 의사가 아닌 경우 이름

    @Column(nullable = false)
    private LocalDate scheduleDate; // 근무일

    @Column(nullable = false)
    private LocalTime startTime; // 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 종료 시간

    @Column(nullable = false, length = 50)
    private String shiftType; // DAY, NIGHT, EVENING, ON_CALL

    @Column(length = 200)
    private String department; // 부서

    @Column(length = 200)
    private String location; // 근무 장소

    @Column(length = 50)
    private String status; // SCHEDULED, CONFIRMED, CANCELLED, COMPLETED

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private StaffSchedule(Doctor doctor, String staffType, String staffName, LocalDate scheduleDate,
                         LocalTime startTime, LocalTime endTime, String shiftType, String department,
                         String location, String status, String notes) {
        this.doctor = doctor;
        this.staffType = staffType;
        this.staffName = staffName;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = shiftType;
        this.department = department;
        this.location = location;
        this.status = status;
        this.notes = notes;
    }

    public static StaffSchedule createForDoctor(Doctor doctor, LocalDate scheduleDate, LocalTime startTime,
                                                LocalTime endTime, String shiftType, String department, String location) {
        return new StaffSchedule(doctor, "DOCTOR", null, scheduleDate, startTime, endTime, shiftType,
                department, location, "SCHEDULED", null);
    }

    public static StaffSchedule createForStaff(String staffName, String staffType, LocalDate scheduleDate,
                                              LocalTime startTime, LocalTime endTime, String shiftType,
                                              String department, String location) {
        return new StaffSchedule(null, staffType, staffName, scheduleDate, startTime, endTime, shiftType,
                department, location, "SCHEDULED", null);
    }

    public void confirm() {
        this.status = "CONFIRMED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void update(LocalTime startTime, LocalTime endTime, String shiftType, String department, String location, String notes) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = shiftType;
        this.department = department;
        this.location = location;
        this.notes = notes;
    }

    public Long getId() {
        return this.scheduleId;
    }
}

