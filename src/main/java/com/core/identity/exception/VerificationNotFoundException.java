package com.core.identity.exception;

public class VerificationNotFoundException extends BaseException{
    public VerificationNotFoundException() {
        super("Təsdiq kodu tapılmadı", 400);
    }
}
