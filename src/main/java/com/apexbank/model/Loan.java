package com.apexbank.model;

import com.apexbank.util.CurrencyUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Domain model representing a Money Loan or Gold Loan in Apex Bank.
 * Demonstrates Object-Oriented Domain Modeling and Encapsulation.
 */
public class Loan {

    private int loanId;
    private String loanReference;
    private int accountId;
    private int customerId;
    private LoanCategory loanCategory;
    private String loanType; // PERSONAL, HOME, VEHICLE, BUSINESS, GOLD_JEWELRY
    private BigDecimal amount;
    private int tenureMonths;
    private BigDecimal interestRate;
    private BigDecimal monthlyEmi;
    private String purpose;
    private BigDecimal monthlyIncome;
    private String employmentType;

    // Gold Loan specific attributes
    private BigDecimal goldWeightGrams;
    private Integer goldKarat;
    private BigDecimal goldMarketRate;
    private String ornamentDetails;

    private LoanStatus status;
    private String adminRemarks;
    private Timestamp appliedDate;
    private Timestamp disbursedAt;

    // Transient attributes for UI presentation
    private String customerName;
    private String accountNumber;

    public Loan() {
        this.status = LoanStatus.PENDING;
        this.appliedDate = new Timestamp(System.currentTimeMillis());
    }

    public static BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRatePercent, int tenureMonths) {
        if (principal == null || tenureMonths <= 0) return BigDecimal.ZERO;
        if (annualRatePercent == null || annualRatePercent.compareTo(BigDecimal.ZERO) <= 0) {
            return principal.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP);
        }
        double p = principal.doubleValue();
        double r = (annualRatePercent.doubleValue() / 100.0) / 12.0;
        int n = tenureMonths;

        double emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }

    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public String getLoanReference() { return loanReference; }
    public void setLoanReference(String loanReference) { this.loanReference = loanReference; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LoanCategory getLoanCategory() { return loanCategory; }
    public void setLoanCategory(LoanCategory loanCategory) { this.loanCategory = loanCategory; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMonthlyEmi() { return monthlyEmi; }
    public void setMonthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public BigDecimal getGoldWeightGrams() { return goldWeightGrams; }
    public void setGoldWeightGrams(BigDecimal goldWeightGrams) { this.goldWeightGrams = goldWeightGrams; }

    public Integer getGoldKarat() { return goldKarat; }
    public void setGoldKarat(Integer goldKarat) { this.goldKarat = goldKarat; }

    public BigDecimal getGoldMarketRate() { return goldMarketRate; }
    public void setGoldMarketRate(BigDecimal goldMarketRate) { this.goldMarketRate = goldMarketRate; }

    public String getOrnamentDetails() { return ornamentDetails; }
    public void setOrnamentDetails(String ornamentDetails) { this.ornamentDetails = ornamentDetails; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }

    public Timestamp getAppliedDate() { return appliedDate; }
    public void setAppliedDate(Timestamp appliedDate) { this.appliedDate = appliedDate; }

    public Timestamp getDisbursedAt() { return disbursedAt; }
    public void setDisbursedAt(Timestamp disbursedAt) { this.disbursedAt = disbursedAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getFormattedAmount() {
        return amount != null ? CurrencyUtil.formatINR(amount) : "₹0.00";
    }

    public String getFormattedEmi() {
        return monthlyEmi != null ? CurrencyUtil.formatINR(monthlyEmi) : "₹0.00";
    }
}
