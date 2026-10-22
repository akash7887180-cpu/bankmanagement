package com.apexbank.dao;

/**
 * Factory class for instantiating Data Access Objects.
 * Demonstrates the Factory Design Pattern.
 */
public class DAOFactory {

    private static final CustomerDAO customerDAO = new CustomerDAOImpl();
    private static final AccountDAO accountDAO = new AccountDAOImpl();
    private static final ATMCardDAO atmCardDAO = new ATMCardDAOImpl();
    private static final TransactionDAO transactionDAO = new TransactionDAOImpl();
    private static final AdminDAO adminDAO = new AdminDAOImpl();
    private static final AuditDAO auditDAO = new AuditDAOImpl();

    public static CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public static AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public static ATMCardDAO getATMCardDAO() {
        return atmCardDAO;
    }

    public static TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }

    public static AdminDAO getAdminDAO() {
        return adminDAO;
    }

    public static AuditDAO getAuditDAO() {
        return auditDAO;
    }
}
