package com.apexbank.exception;

/**
 * Thrown when an ATM withdrawal would exceed the 24-hour daily limit.
 */
public class DailyLimitExceededException extends BankingException {
    private static final long serialVersionUID = 1L;
    private final double attemptedAmount;
    private final double dailyLimit;
    private final double remainingLimit;

    public DailyLimitExceededException(double attemptedAmount, double dailyLimit, double remainingLimit) {
        super(String.format("Daily ATM withdrawal limit exceeded. Attempted: ₹%.2f, Daily Limit: ₹%.2f, Remaining Today: ₹%.2f",
                attemptedAmount, dailyLimit, remainingLimit), "ERR_DAILY_LIMIT_EXCEEDED");
        this.attemptedAmount = attemptedAmount;
        this.dailyLimit = dailyLimit;
        this.remainingLimit = remainingLimit;
    }

    public double getAttemptedAmount() {
        return attemptedAmount;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public double getRemainingLimit() {
        return remainingLimit;
    }
}
