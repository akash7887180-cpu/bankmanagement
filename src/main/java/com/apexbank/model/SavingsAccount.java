package com.apexbank.model;

import com.apexbank.exception.InsufficientFundsException;
import java.sql.Timestamp;

/**
 * Savings Account implementation.
 * Demonstrates OOP Inheritance and Polymorphic Method Overriding.
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    public SavingsAccount() {
        super();
        setMinimumBalance(1000.00);
        setInterestRate(4.00);
        setOverdraftLimit(0.00);
    }

    public SavingsAccount(int accountId, String accountNumber, int customerId, double balance,
                          double minimumBalance, double interestRate, AccountStatus status, Timestamp openedDate) {
        super(accountId, accountNumber, customerId, balance, minimumBalance, interestRate, 0.00, status, openedDate);
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    @Override
    public double calculateInterest(int months) {
        // Simple interest: (Principal * Rate * Time) / (100 * 12)
        return (getBalance() * getInterestRate() * months) / (100.0 * 12.0);
    }

    /**
     * Polymorphic implementation: enforces minimum balance rule.
     */
    @Override
    public boolean validateWithdrawal(double amount) throws InsufficientFundsException {
        double maxPermissibleWithdrawal = getBalance() - getMinimumBalance();
        if (amount > maxPermissibleWithdrawal) {
            throw new InsufficientFundsException(amount, Math.max(0.0, maxPermissibleWithdrawal));
        }
        return true;
    }
}
