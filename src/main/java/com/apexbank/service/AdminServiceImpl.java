package com.apexbank.service;

import com.apexbank.dao.AccountDAO;
import com.apexbank.dao.AdminDAO;
import com.apexbank.dao.ATMCardDAO;
import com.apexbank.dao.AuditDAO;
import com.apexbank.dao.CustomerDAO;
import com.apexbank.dao.DAOFactory;
import com.apexbank.dao.TransactionDAO;
import com.apexbank.exception.BankingException;
import com.apexbank.model.Account;
import com.apexbank.model.AccountStatus;
import com.apexbank.model.Admin;
import com.apexbank.model.ATMCard;
import com.apexbank.model.AuditLog;
import com.apexbank.model.CardStatus;
import com.apexbank.model.Customer;
import com.apexbank.model.LoginHistory;
import com.apexbank.model.Transaction;
import com.apexbank.model.CardType;
import com.apexbank.model.CurrentAccount;
import com.apexbank.model.SavingsAccount;
import com.apexbank.model.TransactionType;
import com.apexbank.util.SecurityUtil;
import com.apexbank.util.ValidationUtil;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Administrative operations implementation.
 */
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO = DAOFactory.getAdminDAO();
    private final CustomerDAO customerDAO = DAOFactory.getCustomerDAO();
    private final AccountDAO accountDAO = DAOFactory.getAccountDAO();
    private final ATMCardDAO cardDAO = DAOFactory.getATMCardDAO();
    private final TransactionDAO txnDAO = DAOFactory.getTransactionDAO();
    private final AuditDAO auditDAO = DAOFactory.getAuditDAO();

    @Override
    public Admin login(String username, String password, String ipAddress) throws BankingException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new BankingException("Username and Password are required.");
        }

        Admin admin = adminDAO.findByUsername(username.trim());
        if (admin == null) {
            auditDAO.logLogin(new LoginHistory("ADMIN", username, ipAddress, "PORTAL", "FAILED_AUTH", "User not found"));
            throw new BankingException("Invalid admin credentials.");
        }

        if (!admin.isActive()) {
            auditDAO.logLogin(new LoginHistory("ADMIN", username, ipAddress, "PORTAL", "ACCOUNT_LOCKED", "Admin user suspended"));
            throw new BankingException("Admin user account is suspended.");
        }

        String inputHash = SecurityUtil.sha256(password.trim());
        if (!inputHash.equalsIgnoreCase(admin.getPasswordHash())) {
            auditDAO.logLogin(new LoginHistory("ADMIN", username, ipAddress, "PORTAL", "FAILED_AUTH", "Incorrect password"));
            throw new BankingException("Invalid admin credentials.");
        }

        adminDAO.updateLastLogin(admin.getAdminId());
        auditDAO.logLogin(new LoginHistory("ADMIN", username, ipAddress, "PORTAL", "SUCCESS", "Admin Login"));
        auditDAO.log("ADMIN_LOGIN", admin.getUsername(), "Logged into administration console", ipAddress);

        return admin;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerDAO.findById(customerId);
    }

    @Override
    public int createCustomer(Customer customer) throws BankingException {
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new BankingException("First name is required.");
        }
        if (!ValidationUtil.isValidEmail(customer.getEmail())) {
            throw new BankingException("Valid email address is required.");
        }
        if (!ValidationUtil.isValidPhone(customer.getPhone())) {
            throw new BankingException("Valid 10-digit phone number is required.");
        }

        int id = customerDAO.create(customer);
        if (id <= 0) {
            throw new BankingException("Failed to register customer. Email or phone may already exist.");
        }
        auditDAO.log("CUSTOMER_CREATE", "ADMIN", "Registered new customer ID: " + id + " (" + customer.getFullName() + ")", "PORTAL");
        return id;
    }

    @Override
    public boolean updateCustomer(Customer customer) throws BankingException {
        boolean ok = customerDAO.update(customer);
        if (ok) {
            auditDAO.log("CUSTOMER_UPDATE", "ADMIN", "Updated customer ID: " + customer.getCustomerId(), "PORTAL");
        }
        return ok;
    }

    @Override
    public boolean deleteCustomer(int customerId) throws BankingException {
        boolean ok = customerDAO.delete(customerId);
        if (ok) {
            auditDAO.log("CUSTOMER_DELETE", "ADMIN", "Deleted customer ID: " + customerId, "PORTAL");
        }
        return ok;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    @Override
    public Account getAccountById(int accountId) {
        return accountDAO.findById(accountId);
    }

    @Override
    public int openAccountAndIssueCard(int customerId, String accountNumber, String accountType, double initialDeposit, String pin, String cardNumber, String cardType, String adminUser) throws BankingException {
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            throw new BankingException("Customer ID " + customerId + " does not exist.");
        }

        if (initialDeposit < 0) {
            throw new BankingException("Initial deposit cannot be negative.");
        }

        if (pin == null || pin.trim().isEmpty()) {
            pin = "1234";
        }
        pin = pin.trim();
        if (!pin.matches("^\\d{4}$")) {
            throw new BankingException("ATM PIN must be exactly 4 digits.");
        }

        Random rnd = new Random();
        String finalAccountNumber;
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            finalAccountNumber = accountNumber.replaceAll("\\D", "").trim();
            if (finalAccountNumber.length() < 6 || finalAccountNumber.length() > 20) {
                throw new BankingException("Account number must be between 6 and 20 digits.");
            }
            if (accountDAO.findByAccountNumber(finalAccountNumber) != null) {
                throw new BankingException("Account number " + finalAccountNumber + " is already in use by another account.");
            }
        } else {
            do {
                finalAccountNumber = "100100" + (1000 + rnd.nextInt(9000));
            } while (accountDAO.findByAccountNumber(finalAccountNumber) != null);
        }

        Account account;
        if ("CURRENT".equalsIgnoreCase(accountType)) {
            account = new CurrentAccount();
            if (initialDeposit < 5000.0) {
                throw new BankingException("Current Account requires minimum initial deposit of ₹5,000.");
            }
        } else {
            account = new SavingsAccount();
            if (initialDeposit < 1000.0) {
                throw new BankingException("Savings Account requires minimum initial deposit of ₹1,000.");
            }
        }

        account.setAccountNumber(finalAccountNumber);
        account.setCustomerId(customerId);
        account.setBalance(initialDeposit);
        account.setStatus(AccountStatus.ACTIVE);

        int accountId = accountDAO.create(account);
        if (accountId <= 0) {
            throw new BankingException("Failed to open bank account in ledger.");
        }

        // Generate or use 16-digit ATM Card Number
        String finalCardNumber;
        if (cardNumber != null && !cardNumber.trim().isEmpty()) {
            finalCardNumber = cardNumber.replaceAll("\\D", "").trim();
            if (finalCardNumber.length() != 16) {
                throw new BankingException("ATM Card Number must be exactly 16 digits.");
            }
            if (cardDAO.findByCardNumber(finalCardNumber) != null) {
                throw new BankingException("Card number " + finalCardNumber + " is already issued.");
            }
        } else {
            do {
                long rand12 = 100000000000L + (long)(rnd.nextDouble() * 899999999999L);
                finalCardNumber = "4532" + rand12;
            } while (cardDAO.findByCardNumber(finalCardNumber) != null);
        }

        String salt = "salt123";
        String pinHash = SecurityUtil.hashWithSalt(pin, salt);

        ATMCard card = new ATMCard();
        card.setCardNumber(finalCardNumber);
        card.setAccountId(accountId);
        card.setCustomerId(customerId);
        card.setCardType(CardType.fromString(cardType));
        card.setPinHash(pinHash);
        card.setPinSalt(salt);
        card.setPinPlain(pin);
        card.setExpiryDate(new Date(System.currentTimeMillis() + 5L * 365 * 24 * 3600 * 1000)); // 5 years validity
        card.setCvvHash("321");
        card.setDailyLimit(40000.00);
        card.setFailedPinAttempts(0);
        card.setStatus(CardStatus.ACTIVE);

        int cardId = cardDAO.create(card);
        if (cardId <= 0) {
            throw new BankingException("Account created (ID: " + accountId + ") but failed to issue ATM Card.");
        }

        // Record Initial Opening Deposit Transaction
        if (initialDeposit > 0) {
            Transaction txn = new Transaction(
                SecurityUtil.generateTxnReference(),
                accountId,
                cardId,
                TransactionType.DEPOSIT,
                initialDeposit,
                initialDeposit,
                "Account Opening Initial Deposit",
                "BRANCH-ADMIN"
            );
            txnDAO.create(txn);
        }

        auditDAO.log("ACCOUNT_OPENED", adminUser != null ? adminUser : "ADMIN",
            "Opened " + account.getAccountType() + " Acc " + finalAccountNumber + " with Card " + finalCardNumber + " (PIN: " + pin + ") for Cust ID " + customerId, "PORTAL");

        return accountId;
    }

    @Override
    public int openAccountAndIssueCard(int customerId, String accountType, double initialDeposit, String pin, String cardType, String adminUser) throws BankingException {
        return openAccountAndIssueCard(customerId, null, accountType, initialDeposit, pin, null, cardType, adminUser);
    }

    @Override
    public boolean changeAccountStatus(int accountId, AccountStatus status, String adminUser) throws BankingException {
        boolean ok = accountDAO.updateStatus(accountId, status);
        if (ok) {
            auditDAO.log("ACCOUNT_STATUS_CHANGE", adminUser, "Set Account ID " + accountId + " status to " + status.name(), "PORTAL");
        }
        return ok;
    }

    @Override
    public List<ATMCard> getAllCards() {
        return cardDAO.findAll();
    }

    @Override
    public boolean changeCardStatus(int cardId, CardStatus status, String adminUser) throws BankingException {
        boolean ok = cardDAO.updateStatus(cardId, status);
        if (ok) {
            auditDAO.log("CARD_STATUS_CHANGE", adminUser, "Set Card ID " + cardId + " status to " + status.name(), "PORTAL");
        }
        return ok;
    }

    @Override
    public boolean resetCardPinAttempts(int cardId, String adminUser) throws BankingException {
        boolean ok = cardDAO.resetFailedAttempts(cardId);
        cardDAO.updateStatus(cardId, CardStatus.ACTIVE);
        if (ok) {
            auditDAO.log("CARD_PIN_RESET", adminUser, "Reset failed PIN attempts and unlocked Card ID: " + cardId, "PORTAL");
        }
        return ok;
    }

    @Override
    public List<Transaction> getAllRecentTransactions(int limit) {
        return txnDAO.findAllRecent(limit);
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerDAO.countTotalCustomers());
        stats.put("totalAccounts", accountDAO.countTotalAccounts());
        stats.put("totalCards", cardDAO.countTotalCards());
        stats.put("totalDeposits", txnDAO.getTotalDeposits());
        stats.put("totalWithdrawals", txnDAO.getTotalWithdrawals());
        stats.put("todayTxnCount", txnDAO.getTodayTransactionCount());
        stats.put("todayVolume", txnDAO.getTodayVolume());
        stats.put("bankTotalBalance", accountDAO.getTotalBankBalance());
        return stats;
    }

    @Override
    public List<AuditLog> getRecentAuditLogs(int limit) {
        return auditDAO.findRecentLogs(limit);
    }

    @Override
    public List<LoginHistory> getRecentLoginHistory(int limit) {
        return auditDAO.findRecentLogins(limit);
    }
}
