package com.core.identity.exception;

public class SuspiciousActivityException extends BaseException {
    public SuspiciousActivityException() {

        super("Şübhəli fəaliyyət! Token və cihaz uyğun gəlmir.",401);
    }
}
