package com.apexbank.service;

import com.apexbank.model.Loan;
import com.apexbank.model.LoanStatus;
import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    Loan applyMoneyLoan(int customerId, int accountId, String loanType, BigDecimal amount,
                        int tenureMonths, BigDecimal annualInterestRate, String purpose,
                        BigDecimal monthlyIncome, String employmentType);

    Loan applyGoldLoan(int customerId, int accountId, BigDecimal goldWeightGrams, int karat,
                       BigDecimal requestedAmount, int tenureMonths, String ornamentDetails);

    List<Loan> getCustomerLoans(int customerId);
    List<Loan> getAccountLoans(int accountId);
    List<Loan> getAllLoans();
    Loan getLoanById(int loanId);

    boolean updateLoanStatus(int loanId, LoanStatus status, String adminRemarks);
    boolean disburseLoan(int loanId, String adminRemarks);

    BigDecimal getGoldRatePerGram(int karat);
    BigDecimal calculateMaxGoldLoan(BigDecimal grams, int karat);
}
