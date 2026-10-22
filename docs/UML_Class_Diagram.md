# UML Class Diagram - Apex ATM Banking Management System

This document provides the formal UML Class Diagram for the **Apex ATM Banking Management System**, showcasing Object-Oriented Programming (OOP) principles: Abstraction, Inheritance, Polymorphism, Encapsulation, Interfaces, and Design Patterns (DAO, Factory, Singleton).

```mermaid
classDiagram
    %% Core Model Hierarchy - Abstraction and Inheritance
    class User {
        <<abstract>>
        -String email
        -String phone
        -Timestamp createdAt
        +getDisplayName()* String
        +getRoleName()* String
        +getEmail() String
        +setEmail(String) void
        +getPhone() String
        +setPhone(String) void
    }

    class Customer {
        -int customerId
        -String firstName
        -String lastName
        -Date dob
        -String gender
        -String address
        -String city
        -String state
        -String pincode
        -String idProofType
        -String idProofNumber
        +Customer()
        +Customer(firstName, lastName, email, phone)
        +Customer(fullProfile...)
        +getDisplayName() String
        +getRoleName() String
        +getFullName() String
    }

    class Admin {
        -int adminId
        -String username
        -String passwordHash
        -String fullName
        -String role
        -boolean isActive
        -Timestamp lastLogin
        +getDisplayName() String
        +getRoleName() String
    }

    User <|-- Customer : Inheritance
    User <|-- Admin : Inheritance

    %% Account Model Hierarchy - Polymorphism & Encapsulation
    class Account {
        <<abstract>>
        -int accountId
        -String accountNumber
        -int customerId
        -double balance
        -double minimumBalance
        -double interestRate
        -double overdraftLimit
        -AccountStatus status
        -Timestamp openedDate
        +getAccountType()* String
        +calculateInterest(int months)* double
        +validateWithdrawal(double amount)* boolean
        +credit(double amount) void
        +debit(double amount) void
        +deposit(double amount) void
        +deposit(double amount, String note) void
    }

    class SavingsAccount {
        +getAccountType() String
        +calculateInterest(int months) double
        +validateWithdrawal(double amount) boolean
    }

    class CurrentAccount {
        +getAccountType() String
        +calculateInterest(int months) double
        +validateWithdrawal(double amount) boolean
    }

    Account <|-- SavingsAccount : Implements (Min Bal ₹1000)
    Account <|-- CurrentAccount : Implements (Overdraft ₹25k)

    %% ATM Card & Transactions
    class ATMCard {
        -int cardId
        -String cardNumber
        -int accountId
        -int customerId
        -CardType cardType
        -String pinHash
        -String pinSalt
        -Date expiryDate
        -double dailyLimit
        -int failedPinAttempts
        -CardStatus status
        +incrementFailedAttempts() boolean
        +resetFailedAttempts() void
        +isLocked() boolean
        +getMaskedCardNumber() String
    }

    class Transaction {
        -int transactionId
        -String referenceNumber
        -int accountId
        -Integer cardId
        -TransactionType transactionType
        -double amount
        -double balanceAfter
        -Integer destinationAccountId
        -String description
        -String channel
        -String atmTerminalId
        -String status
        -Timestamp createdAt
        +getFormattedDate() String
    }

    class Receipt {
        -String receiptNumber
        -String transactionReference
        -String bankName
        -String branchName
        -String atmId
        -String terminalId
        -String maskedCard
        -String maskedAccount
        -String customerName
        -TransactionType transactionType
        -double amount
        -double availableBalance
        +getFormattedDateTime() String
    }

    Customer "1" *-- "1..*" Account : owns
    Account "1" *-- "1..*" ATMCard : linked to
    Account "1" *-- "0..*" Transaction : logs
    Transaction "1" ..> "1" Receipt : generates

    %% Service Layer Interfaces and Implementations
    class AccountOperations {
        <<interface>>
        +deposit(String cardNo, double amount) Receipt
        +withdraw(String cardNo, double amount) Receipt
        +fastCash(String cardNo, double amount) Receipt
        +getBalance(String cardNo) double
    }

    class ATMService {
        <<interface>>
        +authenticate(String cardNo, String pin, String ip, String ua) ATMCard
        +transfer(String srcCard, String targetAcc, double amt, String desc) Receipt
        +getMiniStatement(String cardNo) List~Transaction~
        +getFullStatement(String cardNo, String type, String start, String end, String search) List~Transaction~
        +changePin(String cardNo, String oldPin, String newPin) boolean
        +getRemainingDailyLimit(String cardNo) double
    }

    AccountOperations <|-- ATMService : Extends Interface

    class ATMServiceImpl {
        -CustomerDAO customerDAO
        -AccountDAO accountDAO
        -ATMCardDAO cardDAO
        -TransactionDAO txnDAO
        -AuditDAO auditDAO
        +deposit(...) Receipt
        +withdraw(...) Receipt
        +fastCash(...) Receipt
        +transfer(...) Receipt
    }

    ATMService <|.. ATMServiceImpl : Realization

    %% DAO Layer & Factory Pattern
    class DAOFactory {
        +getCustomerDAO() CustomerDAO$
        +getAccountDAO() AccountDAO$
        +getATMCardDAO() ATMCardDAO$
        +getTransactionDAO() TransactionDAO$
        +getAdminDAO() AdminDAO$
        +getAuditDAO() AuditDAO$
    }

    class DBConnection {
        <<singleton>>
        -DBConnection instance$
        -String url
        -String username
        -String password
        -DBConnection()
        +getInstance() DBConnection$
        +getConnection() Connection
        +close(AutoCloseable... res)$ void
    }

    DAOFactory ..> DBConnection : acquires connections
```

---
*Created as part of the Final Year Capstone Project for Apex ATM Banking Management System.*
