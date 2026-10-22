# Object-Oriented Programming (OOP) Concepts Showcase
## Academic & Viva Defense Reference Guide

This project is intentionally engineered **without Spring Boot or high-level magic frameworks** to demonstrate mastery of core **Java Object-Oriented Programming (OOP)**, Design Patterns, and native Software Engineering principles.

---

### 1. Classes and Objects
* **Concept**: Real-world entities modeled as blueprints (classes) and materialized instances (objects) with state and behavior.
* **Implementation in Code**:
  - `Customer.java`: Models cardholders (`firstName`, `phone`, `dob`, etc.).
  - `ATMCard.java`: Models physical debit cards with EMV chip schemes.
  - `Transaction.java`: Models financial ledger operations.
  - `Receipt.java`: Models printed thermal slips.
* **Code Location**: [`com.apexbank.model`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model)

---

### 2. Encapsulation
* **Concept**: Hiding internal data members through private access modifiers and providing controlled access via public getters, setters, and state validation methods.
* **Implementation in Code**:
  - In `Account.java`, fields `balance`, `minimumBalance`, and `status` are strictly `private`.
  - Mutating balance directly is forbidden; instead, guarded methods like `credit(double amount)` and `debit(double amount)` enforce invariants.
  - In `ATMCard.java`, `failedPinAttempts` is encapsulated. `incrementFailedAttempts()` automatically flips `status = CardStatus.LOCKED` once the threshold reaches 3.
* **Code Location**: [`Account.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Account.java#L58-L68), [`ATMCard.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/ATMCard.java#L47-L56)

---

### 3. Abstraction
* **Concept**: Defining common interfaces and abstract templates without exposing implementation complexities.
* **Implementation in Code**:
  - `User.java`: Abstract class representing system actors. Declares abstract contracts `getDisplayName()` and `getRoleName()`.
  - `Account.java`: Abstract class representing bank accounts. Declares abstract contracts:
    ```java
    public abstract String getAccountType();
    public abstract double calculateInterest(int months);
    public abstract boolean validateWithdrawal(double amount) throws InsufficientFundsException;
    ```
  - `AccountOperations.java`: Interface abstracting the physical ATM terminal functionality.
* **Code Location**: [`User.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/User.java#L23-L24), [`Account.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Account.java#L52-L55)

---

### 4. Inheritance
* **Concept**: Deriving child classes from a parent class to promote reusability and hierarchical taxonomy (`is-a` relationship).
* **Implementation in Code**:
  - `Customer extends User`: Reuses user profile fields and security metadata.
  - `Admin extends User`: Reuses administrative profile fields.
  - `SavingsAccount extends Account`: Inherits account numbering, ledger balance, and credit/debit mechanisms.
  - `CurrentAccount extends Account`: Inherits base account features while adding overdraft capabilities.
  - `BankingException extends Exception`: Base custom checked exception inherited by all domain errors.
* **Code Location**: [`SavingsAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/SavingsAccount.java#L11), [`CurrentAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/CurrentAccount.java#L11)

---

### 5. Polymorphism & Method Overriding (Runtime Polymorphism)
* **Concept**: Providing specialized implementations of an inherited method at runtime depending on the actual underlying object instance.
* **Implementation in Code**:
  - **Dynamic Method Dispatch**:
    ```java
    // Savings Account validates against Minimum Balance (₹1,000)
    @Override
    public boolean validateWithdrawal(double amount) throws InsufficientFundsException {
        double maxPermissibleWithdrawal = getBalance() - getMinimumBalance();
        if (amount > maxPermissibleWithdrawal) {
            throw new InsufficientFundsException(amount, Math.max(0.0, maxPermissibleWithdrawal));
        }
        return true;
    }

    // Current Account validates against Overdraft Limit (Balance + ₹25,000)
    @Override
    public boolean validateWithdrawal(double amount) throws InsufficientFundsException {
        double maxPermissibleWithdrawal = getBalance() + getOverdraftLimit();
        if (amount > maxPermissibleWithdrawal) {
            throw new InsufficientFundsException(amount, maxPermissibleWithdrawal);
        }
        return true;
    }
    ```
  - When `ATMServiceImpl.withdraw()` calls `account.validateWithdrawal(amount)`, the JVM resolves the appropriate rule depending on whether `account` is an instance of `SavingsAccount` or `CurrentAccount`.
* **Code Location**: [`SavingsAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/SavingsAccount.java#L35-L42), [`CurrentAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/CurrentAccount.java#L38-L45)

---

### 6. Constructor Overloading (Compile-Time Polymorphism)
* **Concept**: Defining multiple constructors with different argument lists in the same class.
* **Implementation in Code**:
  - `Customer()`: Default constructor for ORM mapping.
  - `Customer(firstName, lastName, email, phone)`: Quick constructor for registrations.
  - `Customer(customerId, firstName, lastName, email, phone, dob, gender, ...)`: Full profile constructor.
  - `Transaction(...)`: Overloaded across 4 constructors for simple dispense vs inter-account transfer records.
* **Code Location**: [`Customer.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Customer.java#L25-L46), [`Transaction.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Transaction.java#L30-L68)

---

### 7. Method Overloading (Compile-Time Polymorphism)
* **Concept**: Defining methods in the same class with identical names but differing parameter types or counts.
* **Implementation in Code**:
  - `Account.deposit(double amount)` vs `Account.deposit(double amount, String transactionNote)`
  - `SecurityUtil.sha256(String data)` vs `SecurityUtil.hashWithSalt(String data, String salt)`
  - `TransactionDAO.create(Transaction txn)` vs `TransactionDAO.create(Transaction txn, Connection conn)`
* **Code Location**: [`Account.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Account.java#L70-L77), [`TransactionDAO.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/dao/TransactionDAO.java#L11-L12)

---

### 8. Interfaces
* **Concept**: Abstract contracts defining "what" a component must do without prescribing "how".
* **Implementation in Code**:
  - `AccountOperations`: Interface defining fundamental ATM terminal operations.
  - `ATMService`: Extends `AccountOperations` to specify full business orchestration.
  - `CustomerDAO`, `AccountDAO`, `ATMCardDAO`, `TransactionDAO`, `AdminDAO`, `AuditDAO`: Interface separation in the persistence layer.
* **Code Location**: [`AccountOperations.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/AccountOperations.java), [`ATMService.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/ATMService.java)

---

### 9. Exception Handling Hierarchy
* **Concept**: Structured handling of exceptional states using a custom exception class hierarchy.
* **Implementation in Code**:
  - `BankingException`: Base checked domain exception.
  - `InsufficientFundsException`: Thrown when withdrawal exceeds permissible balance.
  - `DailyLimitExceededException`: Thrown when attempted withdrawal breaches the ₹40,000 daily limit.
  - `CardBlockedException`: Thrown when a locked or expired card attempts an operation.
  - `AccountLockedException`: Thrown when interacting with a frozen account.
  - `InvalidPinException`: Thrown on wrong PIN with count of remaining attempts.
* **Code Location**: [`com.apexbank.exception`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/exception)

---

### 10. Collections Framework
* **Concept**: Java Collections Framework for sorting, filtering, and organizing financial data in memory.
* **Implementation in Code**:
  - `List<Transaction>`: Used for statements and ledgers.
  - `Collections.sort(transactions, Comparator.comparing(...))`: Used in `FullStatementServlet` to sort by date or amount ascending/descending.
  - `ConcurrentHashMap<String, String>`: In-memory thread-safe cache used in `ATMServiceImpl` for simulated mobile OTP delivery.
  - `Map<String, Object>`: Used in `AdminService` to bundle dashboard aggregations.
* **Code Location**: [`FullStatementServlet.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/controller/FullStatementServlet.java#L39-L50), [`ATMServiceImpl.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/ATMServiceImpl.java#L38)

---

### 11. File Handling & Stream Management
* **Concept**: Native reading and writing of data streams to and from persistent storage and HTTP response streams.
* **Implementation in Code**:
  - `ConfigReader.java`: Uses `ClassLoader.getResourceAsStream("db.properties")` to load environment properties.
  - `PDFReceiptGenerator.java`: Uses OpenPDF stream writing to generate binary `.pdf` streams piped directly into `HttpServletResponse.getOutputStream()`.
* **Code Location**: [`ConfigReader.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/util/ConfigReader.java#L15), [`PDFReceiptGenerator.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/util/PDFReceiptGenerator.java#L32)

---

### 12. JDBC Connectivity & ACID Transactions
* **Concept**: Direct connection to MySQL using PreparedStatements and manual transaction demarcation (`commit()` / `rollback()`).
* **Implementation in Code**:
  - 100% PreparedStatements across all DAOs: eliminates all SQL injection risk.
  - Atomic Multi-Statement Transactions: In `ATMServiceImpl.transfer()`, both debit and credit statements execute under `connection.setAutoCommit(false)`. If any operation fails, `connection.rollback()` is invoked inside the catch block to guarantee ACID consistency.
* **Code Location**: [`ATMServiceImpl.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/ATMServiceImpl.java#L240-L290)

---

### 13. Software Design Patterns
* **DAO Pattern (Data Access Object)**: Isolates database CRUD queries from service business logic (`CustomerDAO`, `AccountDAO`, `ATMCardDAO`).
* **Factory Pattern**:
  - `DAOFactory.java`: Centralizes instantiation of DAO implementations.
  - `ServiceFactory.java`: Centralizes access to business service singletons.
* **Singleton Pattern**:
  - `DBConnection.java`: Employs double-checked locking to provide a single, thread-safe database connection broker.
* **MVC Pattern (Model-View-Controller)**:
  - **Models**: Plain Java Beans and Domain Entities in `com.apexbank.model`.
  - **Views**: JSP templates with JSTL rendering in `WEB-INF/views/`.
  - **Controllers**: Servlets extending `BaseServlet` handling HTTP requests in `com.apexbank.controller`.
