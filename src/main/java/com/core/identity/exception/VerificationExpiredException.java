package com.core.identity.exception;

public class VerificationExpiredException extends BaseException{
    public VerificationExpiredException() {
        super("Göndərilən təsdiq kodunun müddəti bitib", 400);
    }
}
