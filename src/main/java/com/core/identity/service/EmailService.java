package com.core.identity.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
