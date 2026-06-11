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
@Table(name = "verification", schema = "identity_schema")
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 6, nullable = false)
    private String verificationCode;

    @Column(nullable = false)
    private Instant verificationExpireAt;

    @Column(nullable = true)
    private Instant lastSentAt = Instant.now();

    @Column(nullable = true)
    private Integer sendCount = 0;
}
