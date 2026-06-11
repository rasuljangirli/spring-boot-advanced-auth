package com.core.identity.repository;

import com.core.identity.model.User;
import com.core.identity.model.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, UUID> {

    Optional<Verification> findTopByUserOrderByVerificationExpireAtDesc(User user);

    Optional<Verification> findByVerificationCode(String verificationCode);

    Optional<Verification> findByUser(User user);
}

