package com.emrsystem.global.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("user_session")
public class UserSession {

    @Id
    private String sessionId;

    private String username;

    private String accessToken;

    private LocalDateTime createdAt;

    private LocalDateTime lastAccessedAt;

    private String ipAddress;

    private String userAgent;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl; // Time to live in seconds (default: 30 minutes)
}

