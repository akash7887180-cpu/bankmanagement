package com.apexbank.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class handling cryptographic hashing, OTP simulation, and secure reference generation.
 * Demonstrates OOP Abstraction and Security Best Practices.
 */
public class SecurityUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Hashes an input string using standard SHA-256.
     */
    public static String sha256(String data) {
        if (data == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Hashes with salt.
     */
    public static String hashWithSalt(String data, String salt) {
        if (salt == null || salt.trim().isEmpty()) {
            return sha256(data);
        }
        return sha256(data + ":" + salt);
    }

    /**
     * Verifies an entered PIN against stored hash (supporting both salted and direct SHA-256).
     */
    public static boolean verifyPin(String enteredPin, String storedHash, String storedSalt) {
        if (enteredPin == null || storedHash == null) {
            return false;
        }
        // Direct SHA-256 match
        String rawHash = sha256(enteredPin);
        if (rawHash.equalsIgnoreCase(storedHash)) {
            return true;
        }
        // Salted match
        if (storedSalt != null && !storedSalt.isEmpty()) {
            String saltedHash = hashWithSalt(enteredPin, storedSalt);
            if (saltedHash.equalsIgnoreCase(storedHash)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a 6-digit numeric OTP code.
     */
    public static String generateOtp() {
        int otp = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Generates a unique Transaction Reference Number: TXN-YYYYMMDD-XXXXX
     */
    public static String generateTxnReference() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int randomPart = 10000 + RANDOM.nextInt(90000);
        return "TXN-" + datePart + "-" + randomPart;
    }

    /**
     * Generates a unique Receipt Number: RCPT-YYYYMMDD-XXXXX
     */
    public static String generateReceiptNumber() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int randomPart = 10000 + RANDOM.nextInt(90000);
        return "RCPT-" + datePart + "-" + randomPart;
    }
}
