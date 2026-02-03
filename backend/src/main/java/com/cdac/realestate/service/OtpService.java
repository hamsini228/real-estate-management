package com.cdac.realestate.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    // Storage for OTPs: Email -> OtpEntry
    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private final long OTP_VALID_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    private static class OtpEntry {
        String otp;
        long creationTime;

        OtpEntry(String otp, long creationTime) {
            this.otp = otp;
            this.creationTime = creationTime;
        }
    }

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, new OtpEntry(otp, System.currentTimeMillis()));
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpEntry entry = otpStorage.get(email);
        if (entry == null) {
            return false;
        }

        if (System.currentTimeMillis() - entry.creationTime > OTP_VALID_DURATION_MS) {
            otpStorage.remove(email);
            return false;
        }

        if (entry.otp.equals(otp)) {
            otpStorage.remove(email); // OTP is one-time use
            return true;
        }

        return false;
    }
}
