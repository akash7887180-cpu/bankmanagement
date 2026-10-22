package com.apexbank.exception;

/**
 * Thrown when an account has insufficient funds to fulfill a withdrawal or transfer.
 */
public class InsufficientFundsException extends BankingException {
    private static final long serialVersionUID = 1L;
    private final double requestedAmount;
    private final double availableBalance;

    public InsufficientFundsException(double requestedAmount, double availableBalance) {
        super(String.format("Insufficient funds. Requested: ₹%.2f, Available: ₹%.2f", requestedAmount, availableBalance), "ERR_INSUFFICIENT_FUNDS");
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
