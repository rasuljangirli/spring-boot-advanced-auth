package com.core.identity.repository;

import com.core.identity.model.ThreadErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadErrorLogRepository extends JpaRepository<ThreadErrorLog,Long> {
}
