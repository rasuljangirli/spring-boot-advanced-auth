package com.core.identity.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class VerificationCodeUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generate6DigitCode() {
        int code = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%06d", code);
    }

    public static String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
