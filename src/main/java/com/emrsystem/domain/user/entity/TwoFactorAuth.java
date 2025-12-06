package com.emrsystem.domain.user.entity;

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
@Table(name = "two_factor_auth")
@EntityListeners(AuditingEntityListener.class)
public class TwoFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "two_factor_auth_id")
    private Long twoFactorAuthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private UserAccount userAccount;

    @Column(nullable = false, length = 200)
    private String secretKey; // TOTP 시크릿 키

    @Column(nullable = false)
    private boolean enabled = false; // 2FA 활성화 여부

    @Column(length = 200)
    private String backupCodes; // 백업 코드 (쉼표로 구분)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private TwoFactorAuth(UserAccount userAccount, String secretKey) {
        this.userAccount = userAccount;
        this.secretKey = secretKey;
        this.enabled = false;
    }

    public static TwoFactorAuth create(UserAccount userAccount, String secretKey) {
        return new TwoFactorAuth(userAccount, secretKey);
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void updateBackupCodes(String backupCodes) {
        this.backupCodes = backupCodes;
    }

    public Long getId() {
        return this.twoFactorAuthId;
    }
}

