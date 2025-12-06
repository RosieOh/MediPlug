package com.emrsystem.domain.user.service;

import com.emrsystem.domain.user.entity.UserAccount;
import com.emrsystem.domain.user.repository.UserAccountRepository;
import com.emrsystem.domain.user.request.UserRequests;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccount get(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public UserAccount loadByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public Page<UserAccount> list(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
    }

    public List<UserAccount> listAll() {
        return userAccountRepository.findAll();
    }

    @Transactional
    public UserAccount create(UserRequests.CreateUserRequest request) {
        // 사용자명 중복 체크
        if (userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "이미 존재하는 사용자명입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserAccount userAccount = UserAccount.create(
                request.getUsername(),
                encodedPassword,
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getRoles()
        );

        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount update(Long id, UserRequests.UpdateUserRequest request) {
        UserAccount userAccount = get(id);
        userAccount.update(request.getName(), request.getEmail(), request.getPhone());
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount updatePassword(Long id, UserRequests.UpdatePasswordRequest request) {
        UserAccount userAccount = get(id);

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), userAccount.getPassword())) {
            throw new CommonException(ErrorCode.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userAccount.updatePassword(encodedPassword);
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount updateRoles(Long id, UserRequests.UpdateRolesRequest request) {
        UserAccount userAccount = get(id);
        userAccount.updateRoles(request.getRoles());
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount activate(Long id) {
        UserAccount userAccount = get(id);
        userAccount.activate();
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount deactivate(Long id) {
        UserAccount userAccount = get(id);
        userAccount.deactivate();
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount lock(Long id) {
        UserAccount userAccount = get(id);
        userAccount.lock();
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public UserAccount unlock(Long id) {
        UserAccount userAccount = get(id);
        userAccount.unlock();
        return userAccountRepository.save(userAccount);
    }

    // 기존 메서드 호환성 유지
    @Transactional
    public UserAccount create(String username, String rawPassword, Set<String> roles) {
        String encoded = passwordEncoder.encode(rawPassword);
        return userAccountRepository.save(UserAccount.create(username, encoded, null, null, null, roles));
    }
}


