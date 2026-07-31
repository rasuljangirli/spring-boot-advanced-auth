package com.core.identity.repository;

import com.core.identity.model.PasswordResetVerification;
import com.core.identity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetVerificationRepository extends JpaRepository<PasswordResetVerification, UUID> {

    Optional<PasswordResetVerification> findByUser(User user);

    Optional<PasswordResetVerification> findByOtpCode(String otpCode);

    Optional<PasswordResetVerification> findByResetToken(String resetToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetVerification p WHERE p.user = :user")
    void deleteByUser(User user);

    Optional<PasswordResetVerification> findByOtpCodeAndUser(String otpCode, User user);

}