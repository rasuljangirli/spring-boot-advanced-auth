package com.core.identity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="password_reset_verifications",schema = "identity_schema")
public class PasswordResetVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID id;

    @Column(nullable = false,length = 6)
    private String otpCode;

    @Column(length = 36)
    private String resetToken;

    @Column(nullable = false)
    private boolean isVerified = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = true)
    private Instant lastSentAt = Instant.now();

    @Column(nullable = true)
    private Integer sendCount = 0;

}
