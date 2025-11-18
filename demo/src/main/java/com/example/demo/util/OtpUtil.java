package com.example.demo.util;
import java.security.SecureRandom;

public class OtpUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generate4DigitOtp() {
        return String.valueOf(random.nextInt(9000) + 1000); // 1000–9999
    }
}
