package com.core.identity.util;

import java.util.Random;

public class VerificationCodeUtil {

    public static String generate6DigitCode(){
        return String.format("%06d", new Random().nextInt(1_000_000));
    }
}
