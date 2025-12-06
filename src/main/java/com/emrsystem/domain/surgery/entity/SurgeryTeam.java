package com.emrsystem.domain.surgery.entity;

import com.emrsystem.domain.doctor.entity.Doctor;
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
@Table(name = "surgery_team")
@EntityListeners(AuditingEntityListener.class)
public class SurgeryTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surgery_team_id")
    private Long surgeryTeamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_id", nullable = false)
    private Surgery surgery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, length = 50)
    private String role; // 주의사, 제1조수, 제2조수, 마취의, 간호사 등

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private SurgeryTeam(Surgery surgery, Doctor doctor, String role, String notes) {
        this.surgery = surgery;
        this.doctor = doctor;
        this.role = role;
        this.notes = notes;
    }

    public static SurgeryTeam create(Surgery surgery, Doctor doctor, String role, String notes) {
        return new SurgeryTeam(surgery, doctor, role, notes);
    }

    public void updateRole(String role) {
        this.role = role;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.surgeryTeamId;
    }
}

