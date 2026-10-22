package com.apexbank.exception;

/**
 * Base checked exception for all banking operations.
 * Demonstrates OOP Exception Handling and custom exception hierarchy.
 */
public class BankingException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    public BankingException(String message) {
        super(message);
        this.errorCode = "BANK_ERR_GENERIC";
    }

    public BankingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BankingException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BANK_ERR_CAUSE";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
