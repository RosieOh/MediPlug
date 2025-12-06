package com.emrsystem.global.security.service;

import com.emrsystem.global.security.entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SessionManagementServiceInterface {
    UserSession createSession(String username, String accessToken, HttpServletRequest request);
    void updateLastAccessed(String sessionId);
    void invalidateSession(String sessionId);
    void invalidateAllUserSessions(String username);
    List<UserSession> getUserSessions(String username);
    UserSession getSessionByToken(String accessToken);
}

