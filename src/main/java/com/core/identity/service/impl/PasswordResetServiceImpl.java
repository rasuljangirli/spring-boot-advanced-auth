package com.core.identity.service.impl;

import com.core.identity.dto.request.CompletePasswordResetRequestDTO;
import com.core.identity.dto.request.ResendCodeRequestDTO;
import com.core.identity.dto.request.VerifyCodeRequestDTO;
import com.core.identity.dto.response.PasswordResetTokenResponseDTO;
import com.core.identity.enums.VerificationLimit;
import com.core.identity.exception.UserNotFoundException;
import com.core.identity.exception.TooManyVerificationAttemptsException;
import com.core.identity.exception.VerificationNotExpiredException;
import com.core.identity.model.PasswordResetVerification;
import com.core.identity.model.User;
import com.core.identity.repository.PasswordResetVerificationRepository;
import com.core.identity.service.EmailService;
import com.core.identity.service.PasswordResetService;
import com.core.identity.service.RefreshTokenService;
import com.core.identity.service.UserService;
import com.core.identity.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.identity.exception.VerificationExpiredException;
import com.core.identity.exception.VerificationNotFoundException;
import com.core.identity.exception.InvalidTokenException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetVerificationRepository passwordResetVerificationRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void initiatePasswordReset(ResendCodeRequestDTO resendCodeRequestDTO) {

        User user = userService.findByEmail(resendCodeRequestDTO.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Instant now = Instant.now();

        PasswordResetVerification verification = getOrCreateVerification(user);

        validateVerificationLimits(verification, now);

        updateAndSaveVerification(verification, now);

        emailService.sendEmail(user.getEmail(), "Şifrə yeniləmə təsdiq kodunuz", verification.getOtpCode());
    }

    private PasswordResetVerification getOrCreateVerification(User user) {
        return passwordResetVerificationRepository.findByUser(user)
                .orElseGet(() -> {
                    PasswordResetVerification newVerification = new PasswordResetVerification();
                    newVerification.setUser(user);
                    return newVerification;
                });
    }

    private void validateVerificationLimits(PasswordResetVerification verification, Instant now) {
        if (verification.getId() == null) {
            return;
        }

        if (verification.getSendCount() >= VerificationLimit.maxSendCount()) {
            long blockDurationSeconds = VerificationLimit.waitHours() * 3600L;
            Instant blockExpiryTime = verification.getLastSentAt().plusSeconds(blockDurationSeconds);

            if (now.isBefore(blockExpiryTime)) {
                throw new TooManyVerificationAttemptsException();
            } else {
                verification.setSendCount(0);
            }
        }

        long totalCooldown = VerificationLimit.expirySeconds() - VerificationLimit.timeBufferSeconds();
        Instant allowedNextSendTime = verification.getLastSentAt().plusSeconds(totalCooldown);

        if (now.isBefore(allowedNextSendTime)) {
            throw new VerificationNotExpiredException();
        }
    }

    private void updateAndSaveVerification(PasswordResetVerification verification, Instant now) {
        String otpCode = VerificationCodeUtil.generate6DigitCode();

        verification.setOtpCode(otpCode);
        verification.setVerified(false);
        verification.setResetToken(null);
        verification.setExpiryDate(now.plusSeconds(VerificationLimit.expirySeconds()));
        verification.setLastSentAt(now);
        verification.setSendCount(verification.getSendCount() + 1);

        passwordResetVerificationRepository.save(verification);
    }

    @Override
    @Transactional
    public PasswordResetTokenResponseDTO verifyPasswordResetOtp(VerifyCodeRequestDTO requestDTO) {

        User user = userService.findByEmail(requestDTO.getEmail())
                .orElseThrow(UserNotFoundException::new);

        PasswordResetVerification verification = validateOtpAndGetVerification(requestDTO.getVerificationCode(), user);

        String resetToken = generateAndSaveResetToken(verification);

        return new PasswordResetTokenResponseDTO(resetToken);
    }

    private PasswordResetVerification validateOtpAndGetVerification(String otpCode, User user) {
        PasswordResetVerification verification = passwordResetVerificationRepository
                .findByOtpCodeAndUser(otpCode, user)
                .orElseThrow(VerificationNotFoundException::new);

        if (verification.getExpiryDate().isBefore(Instant.now())) {
            throw new VerificationExpiredException();
        }

        return verification;
    }

    private String generateAndSaveResetToken(PasswordResetVerification verification) {
        String resetToken = VerificationCodeUtil.generateResetToken();

        verification.setVerified(true);
        verification.setResetToken(resetToken);
        verification.setSendCount(0);

        passwordResetVerificationRepository.save(verification);
        return resetToken;
    }

    @Override
    @Transactional
    public void completePasswordReset(CompletePasswordResetRequestDTO requestDTO) {
        PasswordResetVerification verification = passwordResetVerificationRepository
                .findByResetToken(requestDTO.getResetToken())
                .orElseThrow(InvalidTokenException::new);

        if (!verification.isVerified()) {
            throw new InvalidTokenException("Bu əməliyyatı icra etmək üçün öncə OTP kod təsdiqlənməlidir.");
        }

        User user = verification.getUser();
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));

        userService.save(user);

        refreshTokenService.revokeAllUserTokens(user);

        passwordResetVerificationRepository.delete(verification);
    }
}