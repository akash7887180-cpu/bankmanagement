package com.apexbank.exception;

/**
 * Thrown when an ATM card is locked, blocked, or expired.
 */
public class CardBlockedException extends BankingException {
    private static final long serialVersionUID = 1L;

    public CardBlockedException(String message) {
        super(message, "ERR_CARD_BLOCKED");
    }
}
