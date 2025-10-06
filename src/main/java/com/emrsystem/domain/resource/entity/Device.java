package com.emrsystem.domain.resource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    private Device(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Device of(String code, String name) { return new Device(code, name); }

    public void update(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() { return this.deviceId; }
}


