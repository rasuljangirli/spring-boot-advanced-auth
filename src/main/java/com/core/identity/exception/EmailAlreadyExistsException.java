package com.core.identity.exception;

public class EmailAlreadyExistsException extends BaseException{
    public EmailAlreadyExistsException() {
        super("Bu email artıq mövcuddur və təsdiqlənmə gözləyir.", 409);
    }
}
