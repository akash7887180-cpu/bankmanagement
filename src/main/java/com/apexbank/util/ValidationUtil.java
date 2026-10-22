package com.apexbank.util;

import java.util.regex.Pattern;

/**
 * Validates inputs against strict financial formats.
 */
public class ValidationUtil {

    private static final Pattern PIN_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern CARD_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^\\d{9,18}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static boolean isValidPin(String pin) {
        return pin != null && PIN_PATTERN.matcher(pin.trim()).matches();
    }

    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null) return false;
        String digits = cardNumber.replaceAll("[\\s-]+", "");
        return CARD_PATTERN.matcher(digits).matches();
    }

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_PATTERN.matcher(accountNumber.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidAtmAmount(double amount) {
        return amount >= 100.0 && amount <= 50000.0 && (amount % 100.0 == 0);
    }

    public static boolean isPositive(double amount) {
        return amount > 0.0;
    }
}
