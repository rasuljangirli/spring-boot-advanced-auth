package com.core.identity.exception;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException() {
        super("Keçərsiz və ya müddəti bitmiş şifrə sıfırlama tokeni.", 400);
    }

    public InvalidTokenException(String message) {
        super(message, 400);
    }
}