package com.emrsystem.domain.bed.entity;

import com.emrsystem.domain.resource.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bed")
@EntityListeners(AuditingEntityListener.class)
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Long bedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false, length = 20)
    private String bedNumber; // 병상번호

    @Column(nullable = false, length = 50)
    private String bedType; // 일반병상, 중환자실, 격리병상 등

    @Column(nullable = false, length = 50)
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, OUT_OF_ORDER

    @Column(nullable = false)
    private Double dailyRate; // 일일 요금

    @Column(length = 500)
    private String notes; // 특이사항

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Bed(Room room, String bedNumber, String bedType, String status, Double dailyRate) {
        this.room = room;
        this.bedNumber = bedNumber;
        this.bedType = bedType;
        this.status = status;
        this.dailyRate = dailyRate;
    }

    public static Bed create(Room room, String bedNumber, String bedType, Double dailyRate) {
        return new Bed(room, bedNumber, bedType, "AVAILABLE", dailyRate);
    }

    public void occupy() {
        this.status = "OCCUPIED";
    }

    public void release() {
        this.status = "AVAILABLE";
    }

    public void maintenance() {
        this.status = "MAINTENANCE";
    }

    public void outOfOrder() {
        this.status = "OUT_OF_ORDER";
    }

    public void update(String bedType, Double dailyRate, String notes) {
        this.bedType = bedType;
        this.dailyRate = dailyRate;
        this.notes = notes;
    }

    public boolean isAvailable() {
        return "AVAILABLE".equals(this.status);
    }

    public Long getId() { return this.bedId; }

    // Compatibility methods expected by services
    public String getDescription() { return this.notes; }
    public void setMaintenance() { this.maintenance(); }
    public void setAvailable() { this.release(); }
    public void setOccupied() { this.occupy(); }
}
