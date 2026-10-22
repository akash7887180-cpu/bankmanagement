package com.apexbank.exception;

/**
 * Thrown when an account or card is not found in the core banking records.
 */
public class AccountNotFoundException extends BankingException {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String identifier) {
        super("Account or Card record not found for: " + identifier, "ERR_ACCOUNT_NOT_FOUND");
    }
}
