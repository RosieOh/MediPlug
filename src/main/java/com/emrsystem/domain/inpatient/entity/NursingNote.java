package com.emrsystem.domain.inpatient.entity;

import com.emrsystem.domain.bed.entity.Admission;
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
@Table(name = "nursing_note")
@EntityListeners(AuditingEntityListener.class)
public class NursingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nursing_note_id")
    private Long nursingNoteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 기록 일시

    @Column(nullable = false, length = 200)
    private String recordedBy; // 기록한 간호사

    @Column(nullable = false, length = 50)
    private String noteType; // 일반, 투약, 검사, 특이사항 등

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 간병 기록 내용

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private NursingNote(Admission admission, LocalDateTime recordedAt, String recordedBy, String noteType,
                       String content, String notes) {
        this.admission = admission;
        this.recordedAt = recordedAt;
        this.recordedBy = recordedBy;
        this.noteType = noteType;
        this.content = content;
        this.notes = notes;
    }

    public static NursingNote create(Admission admission, LocalDateTime recordedAt, String recordedBy,
                                     String noteType, String content, String notes) {
        return new NursingNote(admission, recordedAt, recordedBy, noteType, content, notes);
    }

    public void update(String noteType, String content, String notes) {
        this.noteType = noteType;
        this.content = content;
        this.notes = notes;
    }

    public Long getId() {
        return this.nursingNoteId;
    }
}

