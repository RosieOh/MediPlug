package com.emrsystem.global.security.service;

import com.emrsystem.domain.user.entity.TwoFactorAuth;
import com.emrsystem.domain.user.entity.UserAccount;
import com.emrsystem.domain.user.repository.TwoFactorAuthRepository;
import com.emrsystem.domain.user.repository.UserAccountRepository;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TwoFactorAuthService {

    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final UserAccountRepository userAccountRepository;
    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    public String generateSecretKey() {
        return secretGenerator.generate();
    }

    public String generateQrCodeUri(String username, String secretKey) {
        QrData data = new QrData.Builder()
                .label(username)
                .secret(secretKey)
                .issuer("EMR System")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        try {
            byte[] qrCodeBytes = qrGenerator.generate(data);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeBytes);
        } catch (QrGenerationException e) {
            log.error("Failed to generate QR code", e);
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "QR 코드 생성에 실패했습니다.");
        }
    }

    public List<String> generateBackupCodes() {
        List<String> codes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            codes.add(String.format("%08d", random.nextInt(100000000)));
        }
        return codes;
    }

    @Transactional
    public TwoFactorAuth setupTwoFactorAuth(Long userAccountId) {
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (twoFactorAuthRepository.existsByUserAccount_Id(userAccountId)) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "이미 2FA가 설정되어 있습니다.");
        }

        String secretKey = generateSecretKey();
        TwoFactorAuth twoFactorAuth = TwoFactorAuth.create(userAccount, secretKey);
        return twoFactorAuthRepository.save(twoFactorAuth);
    }

    @Transactional
    public void enableTwoFactorAuth(Long userAccountId, String code) {
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepository.findByUserAccount_Id(userAccountId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "2FA 설정을 찾을 수 없습니다."));

        if (!verifier.isValidCode(twoFactorAuth.getSecretKey(), code)) {
            throw new CommonException(ErrorCode.UNAUTHORIZED, "인증 코드가 올바르지 않습니다.");
        }

        twoFactorAuth.enable();
        List<String> backupCodes = generateBackupCodes();
        twoFactorAuth.updateBackupCodes(String.join(",", backupCodes));
        twoFactorAuthRepository.save(twoFactorAuth);
    }

    @Transactional
    public void disableTwoFactorAuth(Long userAccountId) {
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepository.findByUserAccount_Id(userAccountId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "2FA 설정을 찾을 수 없습니다."));
        twoFactorAuth.disable();
        twoFactorAuthRepository.save(twoFactorAuth);
    }

    public boolean verifyCode(Long userAccountId, String code) {
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepository.findByUserAccount_Id(userAccountId)
                .orElse(null);

        if (twoFactorAuth == null || !twoFactorAuth.isEnabled()) {
            return false;
        }

        // TOTP 코드 검증
        if (verifier.isValidCode(twoFactorAuth.getSecretKey(), code)) {
            return true;
        }

        // 백업 코드 검증
        if (twoFactorAuth.getBackupCodes() != null) {
            String[] backupCodes = twoFactorAuth.getBackupCodes().split(",");
            for (String backupCode : backupCodes) {
                if (backupCode.trim().equals(code)) {
                    // 사용된 백업 코드 제거
                    List<String> remainingCodes = new ArrayList<>(List.of(backupCodes));
                    remainingCodes.remove(backupCode.trim());
                    twoFactorAuth.updateBackupCodes(String.join(",", remainingCodes));
                    twoFactorAuthRepository.save(twoFactorAuth);
                    return true;
                }
            }
        }

        return false;
    }

    public TwoFactorAuth getTwoFactorAuth(Long userAccountId) {
        return twoFactorAuthRepository.findByUserAccount_Id(userAccountId)
                .orElse(null);
    }
}

