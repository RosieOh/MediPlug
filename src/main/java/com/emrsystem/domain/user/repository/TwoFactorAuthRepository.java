package com.emrsystem.domain.user.repository;

import com.emrsystem.domain.user.entity.TwoFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, Long> {
    Optional<TwoFactorAuth> findByUserAccount_Id(Long userAccountId);
    boolean existsByUserAccount_Id(Long userAccountId);
}

