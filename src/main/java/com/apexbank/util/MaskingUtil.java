package com.apexbank.util;

/**
 * Utility class for PCI-DSS compliant masking of sensitive financial identifiers.
 */
public class MaskingUtil {

    /**
     * Masks a 16-digit card number: "4532-XXXX-XXXX-5678"
     */
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null) return "XXXX-XXXX-XXXX-XXXX";
        String digits = cardNumber.replaceAll("\\D+", "");
        if (digits.length() < 8) {
            return "XXXX-XXXX-XXXX-" + (digits.length() > 4 ? digits.substring(digits.length() - 4) : digits);
        }
        String first4 = digits.substring(0, 4);
        String last4 = digits.substring(digits.length() - 4);
        return first4 + "-XXXX-XXXX-" + last4;
    }

    /**
     * Masks an account number: "100100XXXX" or "XXXXXX4521"
     */
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null) return "XXXXXXXXXX";
        String digits = accountNumber.trim();
        if (digits.length() <= 4) {
            return "XXXX" + digits;
        }
        int visibleCount = 4;
        String prefix = digits.substring(0, Math.min(6, digits.length() - visibleCount));
        String suffix = digits.substring(digits.length() - visibleCount);
        return prefix + "XXXX" + suffix;
    }

    /**
     * Masks an email address: "j***e@example.com"
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "*****@*****.***";
        String[] parts = email.split("@", 2);
        String name = parts[0];
        String domain = parts[1];
        if (name.length() <= 2) {
            return name.charAt(0) + "***@" + domain;
        }
        return name.charAt(0) + "***" + name.charAt(name.length() - 1) + "@" + domain;
    }

    /**
     * Masks a phone number: "XXXXXX3210"
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return "XXXXXXXXXX";
        return "XXXXXX" + phone.substring(phone.length() - 4);
    }
}
