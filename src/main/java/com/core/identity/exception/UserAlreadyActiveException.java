package com.core.identity.exception;

public class UserAlreadyActiveException extends BaseException{

    public UserAlreadyActiveException() {
        super("İstifadəçi artıq aktivdir",400);
    }
}
