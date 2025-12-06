package com.emrsystem.global.security.repository;

import com.emrsystem.global.security.entity.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String> {
    List<UserSession> findByUsername(String username);
    Optional<UserSession> findByAccessToken(String accessToken);
    void deleteByUsername(String username);
}

