package com.core.identity.exception;

public class InvalidVerificationCodeException extends BaseException{
    public InvalidVerificationCodeException() {
        super("Kod düzgün deil", 400);
    }
}
