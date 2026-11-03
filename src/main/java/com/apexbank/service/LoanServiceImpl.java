package com.apexbank.service;

import com.apexbank.dao.AccountDAO;
import com.apexbank.dao.AuditDAO;
import com.apexbank.dao.DAOFactory;
import com.apexbank.dao.LoanDAO;
import com.apexbank.dao.TransactionDAO;
import com.apexbank.model.*;
import com.apexbank.util.CurrencyUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LoanServiceImpl implements LoanService {

    private final LoanDAO loanDAO = DAOFactory.getLoanDAO();
    private final AccountDAO accountDAO = DAOFactory.getAccountDAO();
    private final TransactionDAO transactionDAO = DAOFactory.getTransactionDAO();
    private final AuditDAO auditDAO = DAOFactory.getAuditDAO();

    // Standard live benchmark gold rates in INR per gram
    private static final BigDecimal RATE_24K = new BigDecimal("7500.00");
    private static final BigDecimal RATE_22K = new BigDecimal("6875.00");
    private static final BigDecimal RATE_18K = new BigDecimal("5625.00");
    private static final BigDecimal LTV_RATIO = new BigDecimal("0.75"); // 75% max LTV
    private static final BigDecimal GOLD_LOAN_ANNUAL_RATE = new BigDecimal("9.00"); // 9% p.a.

    @Override
    public Loan applyMoneyLoan(int customerId, int accountId, String loanType, BigDecimal amount,
                              int tenureMonths, BigDecimal annualInterestRate, String purpose,
                              BigDecimal monthlyIncome, String employmentType) {

        if (amount == null || amount.compareTo(new BigDecimal("10000")) < 0) {
            throw new IllegalArgumentException("Minimum loan amount is ₹10,000.");
        }
        if (tenureMonths < 3 || tenureMonths > 360) {
            throw new IllegalArgumentException("Tenure must be between 3 and 360 months.");
        }
        if (annualInterestRate == null || annualInterestRate.compareTo(BigDecimal.ZERO) <= 0) {
            annualInterestRate = new BigDecimal("10.50"); // Default personal loan rate
        }

        BigDecimal emi = Loan.calculateEmi(amount, annualInterestRate, tenureMonths);
        String ref = "LON-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" +
                UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Loan loan = new Loan();
        loan.setLoanReference(ref);
        loan.setCustomerId(customerId);
        loan.setAccountId(accountId);
        loan.setLoanCategory(LoanCategory.MONEY_LOAN);
        loan.setLoanType(loanType != null ? loanType.toUpperCase() : "PERSONAL");
        loan.setAmount(amount);
        loan.setTenureMonths(tenureMonths);
        loan.setInterestRate(annualInterestRate);
        loan.setMonthlyEmi(emi);
        loan.setPurpose(purpose != null ? purpose : "Personal Needs");
        loan.setMonthlyIncome(monthlyIncome != null ? monthlyIncome : BigDecimal.ZERO);
        loan.setEmploymentType(employmentType != null ? employmentType : "SALARIED");
        loan.setStatus(LoanStatus.PENDING);
        loan.setAdminRemarks("Application submitted online. Under underwriting review.");

        boolean created = loanDAO.create(loan);
        if (created) {
            auditDAO.log(new AuditLog("LOAN_APPLIED", "CUSTOMER",
                    "Money loan of " + CurrencyUtil.formatINR(amount) + " applied. Ref: " + ref));
            return loan;
        }
        return null;
    }

    @Override
    public Loan applyGoldLoan(int customerId, int accountId, BigDecimal goldWeightGrams, int karat,
                             BigDecimal requestedAmount, int tenureMonths, String ornamentDetails) {

        if (goldWeightGrams == null || goldWeightGrams.compareTo(new BigDecimal("1.0")) < 0) {
            throw new IllegalArgumentException("Minimum gold weight is 1.0 gram.");
        }
        if (karat != 18 && karat != 22 && karat != 24) {
            throw new IllegalArgumentException("Gold karat purity must be 18, 22, or 24 Karat.");
        }

        BigDecimal marketRate = getGoldRatePerGram(karat);
        BigDecimal maxEligible = calculateMaxGoldLoan(goldWeightGrams, karat);

        if (requestedAmount == null || requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            requestedAmount = maxEligible;
        } else if (requestedAmount.compareTo(maxEligible) > 0) {
            throw new IllegalArgumentException("Requested amount exceeds maximum eligible LTV of " +
                    CurrencyUtil.formatINR(maxEligible) + " for " + goldWeightGrams + "g of " + karat + "K gold.");
        }

        if (tenureMonths <= 0) tenureMonths = 12; // Default 1 year gold loan
        BigDecimal emi = Loan.calculateEmi(requestedAmount, GOLD_LOAN_ANNUAL_RATE, tenureMonths);

        String ref = "GLD-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" +
                UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Loan loan = new Loan();
        loan.setLoanReference(ref);
        loan.setCustomerId(customerId);
        loan.setAccountId(accountId);
        loan.setLoanCategory(LoanCategory.GOLD_LOAN);
        loan.setLoanType("GOLD_PLEDGE");
        loan.setAmount(requestedAmount);
        loan.setTenureMonths(tenureMonths);
        loan.setInterestRate(GOLD_LOAN_ANNUAL_RATE);
        loan.setMonthlyEmi(emi);
        loan.setPurpose("Instant Cash Against Gold Ornaments");
        loan.setGoldWeightGrams(goldWeightGrams);
        loan.setGoldKarat(karat);
        loan.setGoldMarketRate(marketRate);
        loan.setOrnamentDetails(ornamentDetails != null ? ornamentDetails : "Gold Ornaments");
        loan.setStatus(LoanStatus.PENDING);
        loan.setAdminRemarks("Gold pledged: " + goldWeightGrams + "g @ " + karat + "K. Pending branch verification.");

        boolean created = loanDAO.create(loan);
        if (created) {
            auditDAO.log(new AuditLog("GOLD_LOAN_APPLIED", "CUSTOMER",
                    "Gold loan applied for " + CurrencyUtil.formatINR(requestedAmount) + " against " +
                            goldWeightGrams + "g of " + karat + "K gold. Ref: " + ref));
            return loan;
        }
        return null;
    }

    @Override
    public List<Loan> getCustomerLoans(int customerId) {
        return loanDAO.findByCustomerId(customerId);
    }

    @Override
    public List<Loan> getAccountLoans(int accountId) {
        return loanDAO.findByAccountId(accountId);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanDAO.findAll();
    }

    @Override
    public Loan getLoanById(int loanId) {
        return loanDAO.findById(loanId);
    }

    @Override
    public boolean updateLoanStatus(int loanId, LoanStatus status, String adminRemarks) {
        boolean updated = loanDAO.updateStatus(loanId, status, adminRemarks);
        if (updated) {
            auditDAO.log(new AuditLog("LOAN_STATUS_UPDATE", "ADMIN",
                    "Loan ID " + loanId + " updated to " + status + ". Remarks: " + adminRemarks));
        }
        return updated;
    }

    @Override
    public synchronized boolean disburseLoan(int loanId, String adminRemarks) {
        Loan loan = loanDAO.findById(loanId);
        if (loan == null) return false;
        if (loan.getStatus() == LoanStatus.DISBURSED) return true; // Already disbursed

        Account account = accountDAO.findById(loan.getAccountId());
        if (account == null) return false;

        double newBalance = account.getBalance() + loan.getAmount().doubleValue();
        boolean balanceUpdated = accountDAO.updateBalance(account.getAccountId(), newBalance);
        if (!balanceUpdated) return false;

        // Record Disbursal Transaction
        String txnRef = "TXN-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" +
                UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Transaction txn = new Transaction();
        txn.setReferenceNumber(txnRef);
        txn.setAccountId(account.getAccountId());
        txn.setTransactionType(TransactionType.DEPOSIT);
        txn.setAmount(loan.getAmount().doubleValue());
        txn.setBalanceAfter(newBalance);
        txn.setDescription("Direct Credit: Loan Disbursed - " + loan.getLoanReference());
        txn.setChannel("INTERNET_BANKING");
        txn.setAtmTerminalId("APEX-CORE-LOAN");
        txn.setStatus("SUCCESS");

        transactionDAO.create(txn);

        String remarks = (adminRemarks != null && !adminRemarks.trim().isEmpty())
                ? adminRemarks
                : "Funds disbursed successfully into account " + account.getAccountNumber();

        loanDAO.markDisbursed(loanId, remarks);

        auditDAO.log(new AuditLog("LOAN_DISBURSED", "ADMIN",
                "Disbursed " + CurrencyUtil.formatINR(loan.getAmount()) + " to Account " +
                        account.getAccountNumber() + " for Loan " + loan.getLoanReference()));

        return true;
    }

    @Override
    public BigDecimal getGoldRatePerGram(int karat) {
        switch (karat) {
            case 24: return RATE_24K;
            case 22: return RATE_22K;
            case 18:
            default:
                return RATE_18K;
        }
    }

    @Override
    public BigDecimal calculateMaxGoldLoan(BigDecimal grams, int karat) {
        if (grams == null || grams.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;
        BigDecimal rate = getGoldRatePerGram(karat);
        BigDecimal totalGoldValue = grams.multiply(rate);
        return totalGoldValue.multiply(LTV_RATIO).setScale(2, RoundingMode.HALF_UP);
    }
}
