package com.emrsystem.domain.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "hospital")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE hospital SET deleted = true, updated_at = NOW() WHERE hospital_id = ?")
@Where(clause = "deleted = false")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(nullable = false, length = 30)
    private String phone;

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

    private Hospital(String name, String address, String phone, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Hospital create(String name, String address, String phone) {
        LocalDateTime now = LocalDateTime.now();
        return new Hospital(name, address, phone, now, now);
    }

    public void update(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}


