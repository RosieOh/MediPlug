package com.emrsystem.domain.user.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    MASTER_ADMIN("ROLE_MASTER_ADMIN", "마스터 관리자", 1),
    ADMIN("ROLE_ADMIN", "관리자", 2),
    DOCTOR("ROLE_DOCTOR", "의사", 3),
    NURSE("ROLE_NURSE", "간호사", 4);

    private final String code;
    private final String name;
    private final int level; // 낮을수록 높은 권한

    RoleType(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

    /**
     * 현재 역할이 다른 역할보다 높은 권한을 가지고 있는지 확인
     */
    public boolean hasHigherOrEqualAuthority(RoleType other) {
        return this.level <= other.level;
    }

    /**
     * 현재 역할이 다른 역할보다 낮은 권한을 가지고 있는지 확인
     */
    public boolean hasLowerAuthority(RoleType other) {
        return this.level > other.level;
    }

    /**
     * 코드로 RoleType 찾기
     */
    public static RoleType fromCode(String code) {
        for (RoleType roleType : values()) {
            if (roleType.code.equals(code)) {
                return roleType;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }
}

