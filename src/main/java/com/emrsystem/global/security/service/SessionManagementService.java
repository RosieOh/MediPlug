package com.emrsystem.global.security.service;

import com.emrsystem.global.security.entity.UserSession;
import com.emrsystem.global.security.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "true", matchIfMissing = false)
public class SessionManagementService implements SessionManagementServiceInterface {

    private final UserSessionRepository userSessionRepository;
    private static final long SESSION_TTL_SECONDS = 1800; // 30 minutes

    @Transactional
    @Override
    public UserSession createSession(String username, String accessToken, HttpServletRequest request) {
        String sessionId = UUID.randomUUID().toString();
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        UserSession session = UserSession.builder()
                .sessionId(sessionId)
                .username(username)
                .accessToken(accessToken)
                .createdAt(LocalDateTime.now())
                .lastAccessedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .ttl(SESSION_TTL_SECONDS)
                .build();

        return userSessionRepository.save(session);
    }

    @Transactional
    @Override
    public void updateLastAccessed(String sessionId) {
        userSessionRepository.findById(sessionId).ifPresent(session -> {
            session = UserSession.builder()
                    .sessionId(session.getSessionId())
                    .username(session.getUsername())
                    .accessToken(session.getAccessToken())
                    .createdAt(session.getCreatedAt())
                    .lastAccessedAt(LocalDateTime.now())
                    .ipAddress(session.getIpAddress())
                    .userAgent(session.getUserAgent())
                    .ttl(SESSION_TTL_SECONDS)
                    .build();
            userSessionRepository.save(session);
        });
    }

    @Transactional
    @Override
    public void invalidateSession(String sessionId) {
        userSessionRepository.deleteById(sessionId);
    }

    @Transactional
    @Override
    public void invalidateAllUserSessions(String username) {
        userSessionRepository.deleteByUsername(username);
    }

    @Override
    public List<UserSession> getUserSessions(String username) {
        return userSessionRepository.findByUsername(username);
    }

    @Override
    public UserSession getSessionByToken(String accessToken) {
        return userSessionRepository.findByAccessToken(accessToken).orElse(null);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}

// Fallback implementation when Redis is not available
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "false", matchIfMissing = true)
class NoOpSessionManagementService implements SessionManagementServiceInterface {
    
    @Override
    public UserSession createSession(String username, String accessToken, HttpServletRequest request) {
        log.debug("Session management disabled (Redis not available). Session creation skipped for user: {}", username);
        return null;
    }

    @Override
    public void updateLastAccessed(String sessionId) {
        log.debug("Session management disabled (Redis not available). Update skipped for session: {}", sessionId);
    }

    @Override
    public void invalidateSession(String sessionId) {
        log.debug("Session management disabled (Redis not available). Invalidation skipped for session: {}", sessionId);
    }

    @Override
    public void invalidateAllUserSessions(String username) {
        log.debug("Session management disabled (Redis not available). Invalidation skipped for user: {}", username);
    }

    @Override
    public List<UserSession> getUserSessions(String username) {
        return new ArrayList<>();
    }

    @Override
    public UserSession getSessionByToken(String accessToken) {
        return null;
    }
}
