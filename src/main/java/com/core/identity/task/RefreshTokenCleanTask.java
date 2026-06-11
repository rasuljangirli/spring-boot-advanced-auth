package com.core.identity.task;

import lombok.RequiredArgsConstructor;
import com.core.identity.service.RefreshTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanTask {

    private final RefreshTokenService refreshTokenService;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Baku")
    public void cleanExpiredTokens() {
        refreshTokenService.deleteExpiredRefreshTokens();
    }
}