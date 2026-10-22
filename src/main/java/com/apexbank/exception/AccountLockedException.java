package com.apexbank.exception;

/**
 * Thrown when trying to perform transactions on a frozen or locked bank account.
 */
public class AccountLockedException extends BankingException {
    private static final long serialVersionUID = 1L;

    public AccountLockedException(String accountNumber, String status) {
        super("Account " + accountNumber + " is currently " + status + ". Please contact branch management.", "ERR_ACCOUNT_LOCKED");
    }
}
