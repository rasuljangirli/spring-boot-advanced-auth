package com.core.identity.exception;

public class EmailAlreadyVerifiedException extends BaseException {

    public EmailAlreadyVerifiedException() {
        super("Bu email adresi artıq təsdiqlənib. Zəhmət olmasa daxil olun.", 409);
    }
}
