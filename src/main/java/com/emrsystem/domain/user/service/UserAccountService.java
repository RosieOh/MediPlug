package com.emrsystem.domain.user.service;

import com.emrsystem.domain.user.entity.UserAccount;
import com.emrsystem.domain.user.repository.UserAccountRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount create(String username, String rawPassword, Set<String> roles) {
        String encoded = passwordEncoder.encode(rawPassword);
        return userAccountRepository.save(UserAccount.create(username, encoded, roles));
    }

    public UserAccount loadByUsername(String username) {
        return userAccountRepository.findByUsername(username).orElse(null);
    }

    public List<UserAccount> list() { return userAccountRepository.findAll(); }
}


