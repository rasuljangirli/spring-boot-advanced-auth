package com.core.identity.exception;

public class RefreshTokenNotFoundException extends BaseException {
    public RefreshTokenNotFoundException() {

        super("Refresh token tapılmadı!",404);
    }
}
