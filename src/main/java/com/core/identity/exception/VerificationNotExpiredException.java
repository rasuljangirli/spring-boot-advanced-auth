package com.core.identity.exception;

public class VerificationNotExpiredException extends BaseException{

    public VerificationNotExpiredException() {
        super("Kod hələ aktivdir, gözləyin.", 409);
    }
}
