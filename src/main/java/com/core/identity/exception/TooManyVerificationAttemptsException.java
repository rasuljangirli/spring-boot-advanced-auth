package com.core.identity.exception;

public class TooManyVerificationAttemptsException extends BaseException {

    public TooManyVerificationAttemptsException() {
        super("1 saat keçmədən növbəti cəhd edə bilməzsiniz.",429);
    }
}

