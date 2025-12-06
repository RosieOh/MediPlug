package com.emrsystem.domain.emr.entity;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.patient.entity.Patient;
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
@Table(name = "medical_image")
@EntityListeners(AuditingEntityListener.class)
public class MedicalImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id")
    private LabOrder labOrder; // 영상 검사인 경우

    @Column(nullable = false, length = 50)
    private String imageType; // XRAY, CT, MRI, ULTRASOUND, ETC

    @Column(length = 50)
    private String modality; // DICOM 모달리티

    @Column(nullable = false, length = 500)
    private String filePath; // 파일 저장 경로

    @Column(nullable = false, length = 200)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize; // 파일 크기 (bytes)

    @Column(length = 100)
    private String mimeType; // image/jpeg, application/dicom 등

    @Column(nullable = false)
    private LocalDateTime studyDate; // 촬영일시

    @Column(length = 500)
    private String studyDescription; // 검사 설명

    @Column(columnDefinition = "TEXT")
    private String interpretation; // 판독 결과

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiologist_id")
    private Doctor radiologist; // 판독 의사

    @Column(nullable = false, length = 50)
    private String status; // UPLOADED, INTERPRETED, FINALIZED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private MedicalImage(Patient patient, MedicalRecord medicalRecord, LabOrder labOrder, String imageType,
                        String modality, String filePath, String fileName, Long fileSize, String mimeType,
                        LocalDateTime studyDate, String studyDescription, String interpretation, Doctor radiologist,
                        String status, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.patient = patient;
        this.medicalRecord = medicalRecord;
        this.labOrder = labOrder;
        this.imageType = imageType;
        this.modality = modality;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.studyDate = studyDate;
        this.studyDescription = studyDescription;
        this.interpretation = interpretation;
        this.radiologist = radiologist;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MedicalImage create(Patient patient, MedicalRecord medicalRecord, LabOrder labOrder, String imageType,
                                     String modality, String filePath, String fileName, Long fileSize, String mimeType,
                                     LocalDateTime studyDate, String studyDescription) {
        LocalDateTime now = LocalDateTime.now();
        return new MedicalImage(patient, medicalRecord, labOrder, imageType, modality, filePath, fileName, fileSize,
                mimeType, studyDate, studyDescription, null, null, "UPLOADED", null, now, now);
    }

    public void addInterpretation(Doctor radiologist, String interpretation) {
        this.radiologist = radiologist;
        this.interpretation = interpretation;
        this.status = "INTERPRETED";
    }

    public void finalize() {
        this.status = "FINALIZED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.imageId;
    }
}

