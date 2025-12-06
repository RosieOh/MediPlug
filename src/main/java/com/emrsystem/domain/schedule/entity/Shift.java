package com.emrsystem.domain.schedule.entity;

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
@Table(name = "shift")
@EntityListeners(AuditingEntityListener.class)
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long shiftId;

    @Column(nullable = false, length = 50)
    private String shiftType; // DAY, NIGHT, EVENING, ON_CALL

    @Column(nullable = false)
    private LocalTime startTime; // 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 종료 시간

    @Column(nullable = false)
    private int durationHours; // 근무 시간 (시간)

    @Column(length = 200)
    private String description; // 설명

    @Column(nullable = false, length = 50)
    private String status; // ACTIVE, INACTIVE

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Shift(String shiftType, LocalTime startTime, LocalTime endTime, int durationHours,
                 String description, String status) {
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationHours = durationHours;
        this.description = description;
        this.status = status;
    }

    public static Shift create(String shiftType, LocalTime startTime, LocalTime endTime, String description) {
        int durationHours = (int) java.time.Duration.between(startTime, endTime).toHours();
        if (durationHours < 0) {
            durationHours += 24; // 자정을 넘어가는 경우
        }
        return new Shift(shiftType, startTime, endTime, durationHours, description, "ACTIVE");
    }

    public void update(LocalTime startTime, LocalTime endTime, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        int durationHours = (int) java.time.Duration.between(startTime, endTime).toHours();
        if (durationHours < 0) {
            durationHours += 24;
        }
        this.durationHours = durationHours;
        this.description = description;
    }

    public void activate() {
        this.status = "ACTIVE";
    }

    public void deactivate() {
        this.status = "INACTIVE";
    }

    public Long getId() {
        return this.shiftId;
    }
}

