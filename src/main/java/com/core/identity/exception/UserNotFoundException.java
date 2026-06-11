package com.core.identity.exception;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException() {
        super("İstifadəçi tapılmadı", 404);
    }
}
