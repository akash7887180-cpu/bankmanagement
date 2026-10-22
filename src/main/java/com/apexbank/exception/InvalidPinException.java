package com.apexbank.exception;

/**
 * Thrown when an invalid ATM PIN is entered.
 */
public class InvalidPinException extends BankingException {
    private static final long serialVersionUID = 1L;
    private final int remainingAttempts;

    public InvalidPinException(int remainingAttempts) {
        super(remainingAttempts > 0 
                ? "Incorrect PIN. You have " + remainingAttempts + " attempt(s) remaining before your card is locked." 
                : "Incorrect PIN. Maximum attempts exceeded. Your ATM card has been locked for security.",
              "ERR_INVALID_PIN");
        this.remainingAttempts = remainingAttempts;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }
}
