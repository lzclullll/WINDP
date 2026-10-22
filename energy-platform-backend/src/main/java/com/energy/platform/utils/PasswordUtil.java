package com.energy.platform.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordUtil {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private PasswordUtil() {}
    public static String encode(String raw) { return ENCODER.encode(raw); }
    public static boolean matches(String raw, String encoded) { return ENCODER.matches(raw, encoded); }
}
