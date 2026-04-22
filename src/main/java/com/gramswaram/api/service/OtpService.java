package com.gramswaram.api.service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public String generateAndStoreOtp(String phone, String role) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otpStore.put(key(phone, role), otp);
        return otp;
    }

    public boolean verifyOtp(String phone, String role, String otp) {
        String key = key(phone, role);
        String expected = otpStore.get(key);
        boolean valid = expected != null && expected.equals(otp);
        if (valid) {
            otpStore.remove(key);
        }
        return valid;
    }

    private String key(String phone, String role) {
        return (phone == null ? "" : phone.trim()) + "|" + (role == null ? "" : role.trim().toUpperCase());
    }
}
