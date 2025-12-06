package com.emrsystem.domain.inventory.entity;

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
@Table(name = "supplier")
@EntityListeners(AuditingEntityListener.class)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(nullable = false, length = 200)
    private String name; // 공급업체명

    @Column(length = 100)
    private String contactPerson; // 담당자

    @Column(length = 50)
    private String phone; // 전화번호

    @Column(length = 100)
    private String email; // 이메일

    @Column(length = 200)
    private String address; // 주소

    @Column(length = 50)
    private String status; // ACTIVE, INACTIVE

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Supplier(String name, String contactPerson, String phone, String email, String address, String status, String notes) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
        this.notes = notes;
    }

    public static Supplier create(String name, String contactPerson, String phone, String email, String address) {
        return new Supplier(name, contactPerson, phone, email, address, "ACTIVE", null);
    }

    public void update(String name, String contactPerson, String phone, String email, String address, String notes) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.notes = notes;
    }

    public void activate() {
        this.status = "ACTIVE";
    }

    public void deactivate() {
        this.status = "INACTIVE";
    }

    public Long getId() {
        return this.supplierId;
    }
}

