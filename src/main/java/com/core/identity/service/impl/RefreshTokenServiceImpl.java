package com.core.identity.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.core.identity.enums.SecurityLimit;
import com.core.identity.exception.RefreshTokenExpiredException;
import com.core.identity.exception.SuspiciousActivityException;
import com.core.identity.model.RefreshToken;
import com.core.identity.model.User;
import com.core.identity.repository.RefreshTokenRepository;
import com.core.identity.security.jwt.JwtProperties;
import com.core.identity.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken saveRefreshToken(User user, String token, String userAgent) {


        Optional<RefreshToken> existingDeviceToken = refreshTokenRepository.findByUserAndUserAgent(user, userAgent);

        if (existingDeviceToken.isPresent()) {
            refreshTokenRepository.delete(existingDeviceToken.get());
        } else {
            List<RefreshToken> userTokens = refreshTokenRepository.findByUserOrderByExpiryDateAsc(user);

            if (userTokens.size() >= SecurityLimit.maxRefreshTokenDeviceCount()) {
                refreshTokenRepository.delete(userTokens.get(0));
            }
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setUserAgent(userAgent);

        long expirationMs = jwtProperties.getRefreshTokenExpiration();
        refreshToken.setExpiryDate(Instant.now().plusMillis(expirationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(noRollbackFor = { RefreshTokenExpiredException.class, SuspiciousActivityException.class })
    public void verifyExpiration(RefreshToken token, String userAgent) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(token.getToken());

            throw new RefreshTokenExpiredException();
        }

        if (!token.getUserAgent().equals(userAgent)) {
            refreshTokenRepository.deleteByUser(token.getUser());

            throw new SuspiciousActivityException();
        }
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteAllByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public void deleteExpiredRefreshTokens() {
        Instant now = Instant.now();

        long deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(now);

        if (deletedCount > 0) {
            log.info("Cron işi: {} ədəd vaxtı keçmiş refresh token bazadan təmizləndi.", deletedCount);
        }
    }
}