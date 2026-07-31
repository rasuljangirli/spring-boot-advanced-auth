package com.core.identity.service;

import com.core.identity.model.RefreshToken;
import com.core.identity.model.User;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken saveRefreshToken(User user, String token, String userAgent);

    void verifyExpiration(RefreshToken token, String userAgent);

    void deleteByToken(String token);

    void deleteAllByUser(User user);

    void deleteExpiredRefreshTokens();

    void revokeAllUserTokens(User user);
}