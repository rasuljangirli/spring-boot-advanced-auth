package com.core.identity.exception;

public class EmailSendException extends BaseException{
    public EmailSendException() {
        super("Email göndərilərkən xəta baş verdi", 500);
    }
}
