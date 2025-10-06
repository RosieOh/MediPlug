package com.emrsystem.global.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "access_log")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_log_id")
    private Long accessLogId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String resource;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(length = 200)
    private String details;

    @Column(nullable = false)
    private LocalDateTime accessedAt;

    private AccessLog(String username, String resource, String action, String details, LocalDateTime accessedAt) {
        this.username = username;
        this.resource = resource;
        this.action = action;
        this.details = details;
        this.accessedAt = accessedAt;
    }

    public static AccessLog of(String username, String resource, String action, String details) {
        return new AccessLog(username, resource, action, details, LocalDateTime.now());
    }
}


