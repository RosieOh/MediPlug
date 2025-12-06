package com.emrsystem.domain.infection.entity;

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
@Table(name = "infection_prevention")
@EntityListeners(AuditingEntityListener.class)
public class InfectionPrevention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prevention_id")
    private Long preventionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "infection_case_id", nullable = false)
    private InfectionCase infectionCase;

    @Column(nullable = false, length = 200)
    private String measure; // 예방 조치 내용 (예: 손 소독, 격리, 보호구 착용 등)

    @Column(nullable = false)
    private LocalDateTime implementedAt; // 시행 일시

    @Column(length = 200)
    private String implementedBy; // 시행한 사람

    @Column(nullable = false)
    private boolean active = true; // 활성 여부

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private InfectionPrevention(InfectionCase infectionCase, String measure, LocalDateTime implementedAt,
                                String implementedBy, boolean active, String notes) {
        this.infectionCase = infectionCase;
        this.measure = measure;
        this.implementedAt = implementedAt;
        this.implementedBy = implementedBy;
        this.active = active;
        this.notes = notes;
    }

    public static InfectionPrevention create(InfectionCase infectionCase, String measure, String implementedBy) {
        return new InfectionPrevention(infectionCase, measure, LocalDateTime.now(), implementedBy, true, null);
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.preventionId;
    }
}

