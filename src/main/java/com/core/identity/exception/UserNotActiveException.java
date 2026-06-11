package com.core.identity.exception;

public class UserNotActiveException extends BaseException {
    public UserNotActiveException() {
        super("Zəhmət olmasa, əvvəlcə email ünvanınızı təsdiqləyin.", 403);
    }
}