package com.core.identity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "thread_error_log", schema = "identity_schema")
public class ThreadErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID id;

    private String threadName;
    private String errorMessage;

    @CreationTimestamp
    private Instant createdAt;
}
