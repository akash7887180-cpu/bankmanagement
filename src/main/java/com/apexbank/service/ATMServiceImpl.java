package com.apexbank.service;

import com.apexbank.dao.AccountDAO;
import com.apexbank.dao.ATMCardDAO;
import com.apexbank.dao.AuditDAO;
import com.apexbank.dao.CustomerDAO;
import com.apexbank.dao.DAOFactory;
import com.apexbank.dao.TransactionDAO;
import com.apexbank.exception.AccountLockedException;
import com.apexbank.exception.AccountNotFoundException;
import com.apexbank.exception.BankingException;
import com.apexbank.exception.CardBlockedException;
import com.apexbank.exception.DailyLimitExceededException;
import com.apexbank.exception.InsufficientFundsException;
import com.apexbank.exception.InvalidPinException;
import com.apexbank.model.Account;
import com.apexbank.model.AccountStatus;
import com.apexbank.model.ATMCard;
import com.apexbank.model.CardStatus;
import com.apexbank.model.Customer;
import com.apexbank.model.LoginHistory;
import com.apexbank.model.Receipt;
import com.apexbank.model.Transaction;
import com.apexbank.model.TransactionType;
import com.apexbank.util.ConfigReader;
import com.apexbank.util.DBConnection;
import com.apexbank.util.MaskingUtil;
import com.apexbank.util.SecurityUtil;
import com.apexbank.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service implementation for ATM and Cardholder banking workflows.
 * Demonstrates pure OOP business orchestration, Exception Handling, and Transaction ACID management.
 */
public class ATMServiceImpl implements ATMService {

    private final CustomerDAO customerDAO = DAOFactory.getCustomerDAO();
    private final AccountDAO accountDAO = DAOFactory.getAccountDAO();
    private final ATMCardDAO cardDAO = DAOFactory.getATMCardDAO();
    private final TransactionDAO txnDAO = DAOFactory.getTransactionDAO();
    private final AuditDAO auditDAO = DAOFactory.getAuditDAO();

    // In-memory OTP simulation cache: phone -> otp
    private static final ConcurrentHashMap<String, String> OTP_STORE = new ConcurrentHashMap<>();

    private final String atmId = ConfigReader.get("atm.id", "APEX-ATM-042");
    private final String terminalId = ConfigReader.get("atm.terminal", "TERM-DEL-08");
    private final String bankName = ConfigReader.get("atm.bankName", "Apex National Banking Corporation");
    private final String branchName = ConfigReader.get("atm.branch", "Connaught Place Financial Center, New Delhi");
    private final String customerCare = ConfigReader.get("atm.customerCare", "1800-420-APEX (2739)");
    private final double dailyLimit = ConfigReader.getDouble("atm.dailyWithdrawalLimit", 40000.00);

    @Override
    public ATMCard authenticate(String cardNumber, String enteredPin, String ipAddress, String userAgent) throws BankingException {
        if (!ValidationUtil.isValidCardNumber(cardNumber)) {
            throw new BankingException("Invalid card number format. Must be 16 digits.");
        }
        if (!ValidationUtil.isValidPin(enteredPin)) {
            throw new BankingException("Invalid PIN format. Must be exactly 4 digits.");
        }

        ATMCard card = cardDAO.findByCardNumber(cardNumber);
        if (card == null) {
            auditDAO.logLogin(new LoginHistory("CUSTOMER", cardNumber, ipAddress, userAgent, "FAILED_AUTH", "Card number does not exist"));
            throw new AccountNotFoundException("ATM Card not recognized by network.");
        }

        if (card.getStatus() == CardStatus.LOCKED || card.getFailedPinAttempts() >= ATMCard.MAX_FAILED_ATTEMPTS) {
            auditDAO.logLogin(new LoginHistory("CUSTOMER", cardNumber, ipAddress, userAgent, "ACCOUNT_LOCKED", "Card is locked"));
            throw new CardBlockedException("Your ATM card is currently LOCKED due to multiple incorrect PIN attempts. Please visit a branch or use OTP Unlock.");
        }

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardBlockedException("Your ATM card is currently " + card.getStatus() + ". Please contact customer service.");
        }

        Account account = accountDAO.findById(card.getAccountId());
        if (account == null || account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountLockedException(account != null ? account.getAccountNumber() : "UNKNOWN", account != null ? account.getStatus().name() : "INACTIVE");
        }

        boolean pinMatches = SecurityUtil.verifyPin(enteredPin, card.getPinHash(), card.getPinSalt());
        if (!pinMatches) {
            int currentFailures = cardDAO.incrementFailedAttempts(card.getCardId());
            int remaining = Math.max(0, ATMCard.MAX_FAILED_ATTEMPTS - currentFailures);
            auditDAO.logLogin(new LoginHistory("CUSTOMER", cardNumber, ipAddress, userAgent, "FAILED_PIN", "Incorrect PIN entered. Failures: " + currentFailures));
            throw new InvalidPinException(remaining);
        }

        // Reset failed count on successful PIN
        cardDAO.resetFailedAttempts(card.getCardId());
        auditDAO.logLogin(new LoginHistory("CUSTOMER", cardNumber, ipAddress, userAgent, "SUCCESS", "ATM Login successful"));
        auditDAO.log("ATM_LOGIN_SUCCESS", "Card:" + card.getMaskedCardNumber(), "Cardholder logged into ATM kiosk", ipAddress);

        return card;
    }

    @Override
    public Receipt deposit(String cardNumber, double amount) throws BankingException {
        if (!ValidationUtil.isValidAtmAmount(amount)) {
            throw new BankingException("Deposit amount must be at least ₹100 and a multiple of ₹100.");
        }

        ATMCard card = getValidCard(cardNumber);
        Account account = getValidAccount(card.getAccountId());
        Customer customer = customerDAO.findById(card.getCustomerId());

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Begin ACID Transaction

            double newBalance = account.getBalance() + amount;
            account.credit(amount);

            boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance, conn);
            if (!updated) {
                conn.rollback();
                throw new BankingException("Deposit transaction failed: ledger balance could not be updated.");
            }

            Transaction txn = new Transaction(
                    SecurityUtil.generateTxnReference(),
                    account.getAccountId(),
                    card.getCardId(),
                    TransactionType.DEPOSIT,
                    amount,
                    newBalance,
                    "ATM Cash Deposit",
                    this.atmId
            );

            int txnId = txnDAO.create(txn, conn);
            if (txnId <= 0) {
                conn.rollback();
                throw new BankingException("Deposit transaction failed: transaction record could not be persisted.");
            }

            conn.commit(); // Commit Transaction

            auditDAO.log("CASH_DEPOSIT", "Card:" + card.getMaskedCardNumber(), "Deposited ₹" + amount + ", New Bal: ₹" + newBalance, "ATM");

            return generateReceipt(txn, account, customer, card);
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new BankingException("Database transaction error during deposit: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public Receipt withdraw(String cardNumber, double amount) throws BankingException {
        if (!ValidationUtil.isValidAtmAmount(amount)) {
            throw new BankingException("Withdrawal amount must be in multiples of ₹100 or ₹500 (Max ₹50,000 per transaction).");
        }

        ATMCard card = getValidCard(cardNumber);
        Account account = getValidAccount(card.getAccountId());
        Customer customer = customerDAO.findById(card.getCustomerId());

        // Check Polymorphic Account Withdrawal Rule (Savings min balance vs Current overdraft)
        account.validateWithdrawal(amount);

        // Check 24-hour Daily Withdrawal Limit
        double todayWithdrawn = cardDAO.getTodayWithdrawalTotal(card.getCardId());
        double remainingDailyLimit = Math.max(0.0, this.dailyLimit - todayWithdrawn);
        if (amount > remainingDailyLimit) {
            throw new DailyLimitExceededException(amount, this.dailyLimit, remainingDailyLimit);
        }

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Begin ACID Transaction

            double newBalance = account.getBalance() - amount;
            account.debit(amount);

            boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance, conn);
            if (!updated) {
                conn.rollback();
                throw new BankingException("Withdrawal failed: ledger balance could not be updated.");
            }

            Transaction txn = new Transaction(
                    SecurityUtil.generateTxnReference(),
                    account.getAccountId(),
                    card.getCardId(),
                    TransactionType.WITHDRAWAL,
                    amount,
                    newBalance,
                    "ATM Cash Withdrawal",
                    this.atmId
            );

            int txnId = txnDAO.create(txn, conn);
            if (txnId <= 0) {
                conn.rollback();
                throw new BankingException("Withdrawal failed: could not record transaction.");
            }

            conn.commit(); // Commit Transaction

            auditDAO.log("CASH_WITHDRAWAL", "Card:" + card.getMaskedCardNumber(), "Dispensed ₹" + amount + ", New Bal: ₹" + newBalance, "ATM");

            return generateReceipt(txn, account, customer, card);
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new BankingException("Database error during cash withdrawal: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public Receipt fastCash(String cardNumber, double amount) throws BankingException {
        // Fast cash is a 1-click withdrawal with predefined denomination amounts
        if (amount != 500 && amount != 1000 && amount != 2000 && amount != 5000 && amount != 10000) {
            throw new BankingException("Invalid Fast Cash denomination. Allowed: ₹500, ₹1000, ₹2000, ₹5000, ₹10000.");
        }

        ATMCard card = getValidCard(cardNumber);
        Account account = getValidAccount(card.getAccountId());
        Customer customer = customerDAO.findById(card.getCustomerId());

        account.validateWithdrawal(amount);

        double todayWithdrawn = cardDAO.getTodayWithdrawalTotal(card.getCardId());
        double remainingDailyLimit = Math.max(0.0, this.dailyLimit - todayWithdrawn);
        if (amount > remainingDailyLimit) {
            throw new DailyLimitExceededException(amount, this.dailyLimit, remainingDailyLimit);
        }

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            double newBalance = account.getBalance() - amount;
            account.debit(amount);

            boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance, conn);
            if (!updated) {
                conn.rollback();
                throw new BankingException("Fast Cash dispense failed: balance could not be deducted.");
            }

            Transaction txn = new Transaction(
                    SecurityUtil.generateTxnReference(),
                    account.getAccountId(),
                    card.getCardId(),
                    TransactionType.FAST_CASH,
                    amount,
                    newBalance,
                    "ATM Fast Cash Dispense",
                    this.atmId
            );

            int txnId = txnDAO.create(txn, conn);
            if (txnId <= 0) {
                conn.rollback();
                throw new BankingException("Fast Cash failed: could not record transaction.");
            }

            conn.commit();

            auditDAO.log("FAST_CASH", "Card:" + card.getMaskedCardNumber(), "Fast Cash Dispense ₹" + amount, "ATM");

            return generateReceipt(txn, account, customer, card);
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new BankingException("Database error during Fast Cash: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public Receipt transfer(String sourceCardNumber, String targetAccountNumber, double amount, String description) throws BankingException {
        if (!ValidationUtil.isPositive(amount) || amount < 100.0) {
            throw new BankingException("Transfer amount must be at least ₹100.00.");
        }

        ATMCard sourceCard = getValidCard(sourceCardNumber);
        Account sourceAccount = getValidAccount(sourceCard.getAccountId());
        Customer sourceCustomer = customerDAO.findById(sourceCard.getCustomerId());

        if (sourceAccount.getAccountNumber().equalsIgnoreCase(targetAccountNumber.trim())) {
            throw new BankingException("Cannot transfer funds to the same source account.");
        }

        Account targetAccount = accountDAO.findByAccountNumber(targetAccountNumber.trim());
        if (targetAccount == null) {
            throw new AccountNotFoundException("Beneficiary account number '" + targetAccountNumber + "' does not exist.");
        }

        if (targetAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountLockedException(targetAccountNumber, targetAccount.getStatus().name());
        }

        // Validate sender's available balance (Polymorphism)
        sourceAccount.validateWithdrawal(amount);

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // ATOMIC MULTI-ACCOUNT TRANSFER

            double senderNewBal = sourceAccount.getBalance() - amount;
            double recipientNewBal = targetAccount.getBalance() + amount;

            // 1. Debit Source Account
            boolean debited = accountDAO.updateBalance(sourceAccount.getAccountId(), senderNewBal, conn);
            if (!debited) {
                conn.rollback();
                throw new BankingException("Transfer failed: could not debit source account.");
            }

            // 2. Credit Destination Account
            boolean credited = accountDAO.updateBalance(targetAccount.getAccountId(), recipientNewBal, conn);
            if (!credited) {
                conn.rollback();
                throw new BankingException("Transfer failed: could not credit beneficiary account.");
            }

            String refNo = SecurityUtil.generateTxnReference();

            // 3. Insert Outward Transaction for Source
            Transaction debitTxn = new Transaction(
                    refNo,
                    sourceAccount.getAccountId(),
                    sourceCard.getCardId(),
                    TransactionType.TRANSFER_OUT,
                    amount,
                    senderNewBal,
                    targetAccount.getAccountId(),
                    "Fund Transfer to " + MaskingUtil.maskAccountNumber(targetAccount.getAccountNumber()) + (description != null ? " - " + description : ""),
                    this.atmId
            );
            txnDAO.create(debitTxn, conn);

            // 4. Insert Inward Transaction for Recipient
            Transaction creditTxn = new Transaction(
                    refNo + "-IN",
                    targetAccount.getAccountId(),
                    null,
                    TransactionType.TRANSFER_IN,
                    amount,
                    recipientNewBal,
                    sourceAccount.getAccountId(),
                    "Fund Received from " + MaskingUtil.maskAccountNumber(sourceAccount.getAccountNumber()),
                    this.atmId
            );
            txnDAO.create(creditTxn, conn);

            conn.commit(); // ATOMIC COMMIT

            auditDAO.log("FUND_TRANSFER", "Card:" + sourceCard.getMaskedCardNumber(),
                    "Transferred ₹" + amount + " to Acc " + targetAccountNumber, "ATM");

            return generateReceipt(debitTxn, sourceAccount, sourceCustomer, sourceCard);
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new BankingException("Transfer failed due to internal error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public double getBalance(String cardNumber) throws BankingException {
        ATMCard card = getValidCard(cardNumber);
        Account account = getValidAccount(card.getAccountId());
        return account.getBalance();
    }

    @Override
    public List<Transaction> getMiniStatement(String cardNumber) throws BankingException {
        ATMCard card = getValidCard(cardNumber);
        return txnDAO.findLastTransactions(card.getAccountId(), 10);
    }

    @Override
    public List<Transaction> getFullStatement(String cardNumber, String type, String startDate, String endDate, String search) throws BankingException {
        ATMCard card = getValidCard(cardNumber);
        return txnDAO.findFiltered(card.getAccountId(), type, startDate, endDate, search);
    }

    @Override
    public boolean changePin(String cardNumber, String oldPin, String newPin) throws BankingException {
        if (!ValidationUtil.isValidPin(newPin)) {
            throw new BankingException("New PIN must be exactly 4 numeric digits.");
        }
        if (oldPin != null && oldPin.trim().equals(newPin.trim())) {
            throw new BankingException("New PIN cannot be the same as the old PIN.");
        }

        ATMCard card = getValidCard(cardNumber);

        boolean oldMatches = SecurityUtil.verifyPin(oldPin, card.getPinHash(), card.getPinSalt());
        if (!oldMatches) {
            throw new InvalidPinException(1);
        }

        String salt = "salt_" + System.currentTimeMillis();
        String newHash = SecurityUtil.hashWithSalt(newPin.trim(), salt);

        boolean updated = cardDAO.updatePin(card.getCardId(), newHash, salt, newPin.trim());
        if (updated) {
            auditDAO.log("PIN_CHANGE", "Card:" + card.getMaskedCardNumber(), "ATM PIN successfully changed", "ATM");
        }
        return updated;
    }

    @Override
    public Customer getCustomerByCard(String cardNumber) throws BankingException {
        ATMCard card = getValidCard(cardNumber);
        Customer c = customerDAO.findById(card.getCustomerId());
        if (c == null) throw new AccountNotFoundException("Customer profile not found");
        return c;
    }

    @Override
    public Account getAccountByCard(String cardNumber) throws BankingException {
        ATMCard card = getValidCard(cardNumber);
        return getValidAccount(card.getAccountId());
    }

    @Override
    public Receipt generateReceipt(Transaction txn, Account acc, Customer cust, ATMCard card) {
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(SecurityUtil.generateReceiptNumber());
        receipt.setTransactionId(txn.getTransactionId());
        receipt.setTransactionReference(txn.getReferenceNumber());
        receipt.setBankName(this.bankName);
        receipt.setBranchName(this.branchName);
        receipt.setAtmId(this.atmId);
        receipt.setTerminalId(this.terminalId);
        receipt.setCustomerCarePhone(this.customerCare);
        receipt.setCustomerName(cust != null ? cust.getFullName() : "VALUED CUSTOMER");
        receipt.setMaskedAccount(acc != null ? MaskingUtil.maskAccountNumber(acc.getAccountNumber()) : "XXXXXXXXXX");
        receipt.setMaskedCard(card != null ? card.getMaskedCardNumber() : "XXXX-XXXX-XXXX-XXXX");
        receipt.setTransactionType(txn.getTransactionType());
        receipt.setAmount(txn.getAmount());
        receipt.setAvailableBalance(txn.getBalanceAfter());
        receipt.setTimestamp(txn.getCreatedAt());
        return receipt;
    }

    @Override
    public boolean verifyOtp(String phone, String otp) {
        if (phone == null || otp == null) return false;
        String cleanPhone = phone.replaceAll("\\D+", "");
        String stored = OTP_STORE.get(cleanPhone);
        if (stored != null && stored.equals(otp.trim())) {
            OTP_STORE.remove(cleanPhone);
            return true;
        }
        return false;
    }

    @Override
    public String triggerOtp(String phone, String purpose) {
        String cleanPhone = phone.replaceAll("\\D+", "");
        String otp = SecurityUtil.generateOtp();
        OTP_STORE.put(cleanPhone, otp);
        auditDAO.log("OTP_SENT", "Phone:" + MaskingUtil.maskPhone(cleanPhone), "Simulated OTP: " + otp + " for " + purpose, "ATM");
        return otp;
    }

    @Override
    public double getRemainingDailyLimit(String cardNumber) throws BankingException {
        ATMCard card = getValidCard(cardNumber);
        double todayWithdrawn = cardDAO.getTodayWithdrawalTotal(card.getCardId());
        return Math.max(0.0, this.dailyLimit - todayWithdrawn);
    }

    private ATMCard getValidCard(String cardNumber) throws BankingException {
        ATMCard card = cardDAO.findByCardNumber(cardNumber);
        if (card == null) {
            throw new AccountNotFoundException("Card not found: " + cardNumber);
        }
        if (card.isLocked() || card.getStatus() != CardStatus.ACTIVE) {
            throw new CardBlockedException("ATM card is inactive or blocked.");
        }
        return card;
    }

    private Account getValidAccount(int accountId) throws BankingException {
        Account acc = accountDAO.findById(accountId);
        if (acc == null) {
            throw new AccountNotFoundException("Account record not found.");
        }
        if (acc.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountLockedException(acc.getAccountNumber(), acc.getStatus().name());
        }
        return acc;
    }
}
