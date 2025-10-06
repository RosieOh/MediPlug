package com.emrsystem.domain.doctor.entity;

import com.emrsystem.domain.department.entity.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "doctor")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE doctor SET deleted = true, updated_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 100, unique = true)
    private String licenseNumber;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String updatedBy;

    @Column(nullable = false)
    private boolean deleted = false;

    private Doctor(Department department, String name, String phone, String licenseNumber, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.department = department;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Doctor create(Department department, String name, String phone, String licenseNumber) {
        LocalDateTime now = LocalDateTime.now();
        return new Doctor(department, name, phone, licenseNumber, now, now);
    }

    public void update(Department department, String name, String phone, String licenseNumber) {
        this.department = department;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }
}


