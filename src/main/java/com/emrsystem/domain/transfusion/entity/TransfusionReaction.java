package com.emrsystem.domain.transfusion.entity;

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
@Table(name = "transfusion_reaction")
@EntityListeners(AuditingEntityListener.class)
public class TransfusionReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long reactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfusion_record_id", nullable = false)
    private TransfusionRecord transfusionRecord;

    @Column(nullable = false, length = 50)
    private String reactionType; // 알레르기 반응, 발열 반응, 용혈 반응 등

    @Column(nullable = false, length = 50)
    private String severity; // MILD, MODERATE, SEVERE, LIFE_THREATENING

    @Column(nullable = false)
    private LocalDateTime occurredAt; // 발생 일시

    @Column(columnDefinition = "TEXT", nullable = false)
    private String symptoms; // 증상

    @Column(columnDefinition = "TEXT")
    private String treatment; // 치료 방법

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private TransfusionReaction(TransfusionRecord transfusionRecord, String reactionType, String severity,
                               LocalDateTime occurredAt, String symptoms, String treatment, String notes) {
        this.transfusionRecord = transfusionRecord;
        this.reactionType = reactionType;
        this.severity = severity;
        this.occurredAt = occurredAt;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.notes = notes;
    }

    public static TransfusionReaction create(TransfusionRecord transfusionRecord, String reactionType, String severity,
                                            String symptoms, String treatment) {
        return new TransfusionReaction(transfusionRecord, reactionType, severity, LocalDateTime.now(),
                symptoms, treatment, null);
    }

    public void update(String reactionType, String severity, String symptoms, String treatment, String notes) {
        this.reactionType = reactionType;
        this.severity = severity;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.notes = notes;
    }

    public Long getId() {
        return this.reactionId;
    }
}

