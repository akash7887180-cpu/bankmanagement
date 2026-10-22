# Apex ATM Banking Management System
### Enterprise ATM & Core Internet Banking System (Java OOP Final Year Engineering Project)

[![Java](https://img.shields.io/badge/Java-8%20%7C%2011%20%7C%2017-blue.svg)](https://www.oracle.com/java/)
[![Backend](https://img.shields.io/badge/Architecture-MVC%20%7C%20Servlets%20%7C%20JSP%20%7C%20JDBC-orange.svg)](https://tomcat.apache.org/)
[![Database](https://img.shields.io/badge/Database-MySQL%205.7%2B%20%2F%208.0%2B-blue.svg)](https://www.mysql.com/)
[![Frontend](https://img.shields.io/badge/Frontend-Bootstrap%205%20%7C%20HTML5%20%7C%20CSS3%20%7C%20WebAudio-green.svg)](https://getbootstrap.com/)
[![Frameworks](https://img.shields.io/badge/Frameworks-Zero%20Frameworks%20(Pure%20OOP)-red.svg)](#)

---

## 🏛️ Executive Summary

**Apex ATM Banking Management System** is an industry-grade, enterprise-ready banking software developed as a **Final Year Engineering Capstone Project**. Unlike basic CRUD demonstrations, this project combines an authentic, tactile **Physical ATM Machine Simulator** (with phosphor CRT screen, side bezel push-buttons, illuminated slots, and synthesized metallic key-click audio) with a **Modern Internet Banking Portal** and an **Executive Administration Console**.

The entire backend is built **strictly using Java Servlets, JSP, JDBC, and pure Object-Oriented Programming (OOP) concepts without Spring Boot or external frameworks**, providing full academic defensibility for university evaluation, code audits, and viva examinations.

---

## 🚀 Key Modules & System Capabilities

### 1. 💳 Dual Customer Interface
* **Realistic ATM Kiosk Simulator**:
  - Heavy graphite/brushed-metal ATM chassis.
  - Emerald phosphor CRT screen with scanline overlay effects.
  - 8 physical bezel push-buttons aligned with on-screen operations.
  - Flashing green LED card insertion slot & illuminated cash dispenser shutter.
  - 16-key tactile metallic keypad with **synthesized real-time audio** via HTML5 Web Audio API (zero audio files needed!).
* **Modern Internet Banking Dashboard**:
  - Glassmorphic balance cards with privacy toggle (show/hide balance).
  - Masked ATM Card display (`4532-XXXX-XXXX-5678`) and Account Number (`100100XXXX`).
  - Dark Mode and Light Mode theme switcher with local state persistence.
  - 24-hour daily withdrawal limit tracking bar (₹40,000 daily limit).

### 2. 🔐 Security & Authentication Module
* **PIN Encryption**: Salted SHA-256 cryptographic hashing.
* **Account / Card Auto-Lock**: Card automatically locks after **3 consecutive incorrect PIN attempts**.
* **Simulated OTP Engine**: 6-digit SMS OTP simulation for forgot PIN workflows and card unlocks.
* **HTTP Security Filters**: XSS protection headers, Clickjacking defense (`X-Frame-Options: SAMEORIGIN`), and session timeout invalidation.

### 3. 💵 Complete ATM Operations
* **Cash Deposit**: Validates amounts in multiples of ₹100, updates ledger balance immediately, generates thermal receipt.
* **Cash Withdrawal**: Polymorphic validation (Savings enforces ₹1,000 minimum balance; Current allows ₹25,000 overdraft), verifies ₹40,000 daily limit, dispenses notes.
* **Fast Cash (1-Click)**: Instant dispense buttons for **₹500, ₹1,000, ₹2,000, ₹5,000, and ₹10,000**.
* **Balance Enquiry**: Displays live balance formatted in Indian Rupees (`₹ 58,450.00`) and formal words (*"Rupees Fifty-Eight Thousand Four Hundred Fifty Only"*).
* **Mini Statement**: Displays the last 10 account movements with status badges.
* **Full Statement**: Search by keyword/reference, filter by transaction type and date range, sort by date/amount, print view, and **Server-Side PDF export**.
* **Inter-Account Money Transfer**: Atomic multi-account transfer with **ACID rollback guarantee** on database errors.
* **Change ATM PIN**: Secure update validating old PIN and confirming new 4-digit PIN.

### 4. 🧾 Authentic ATM Thermal Receipt & PDF Engine
* Replicates physical receipts issued by banks such as SBI, HDFC, ICICI, and Axis Bank.
* Zigzag torn paper edges, monospace thermal font, transaction reference, terminal ID, masked card/account, and barcode simulation.
* Action Buttons: **Download PDF** (native OpenPDF binary stream), **Print Receipt**, and **Share Receipt**.

### 5. 🛡️ Executive Administration Panel
* **Customer Directory**: Add new customers with KYC ID proofs, edit records, delete customers.
* **Account Controls**: **Freeze Account** (instantly halts withdrawals/transfers), **Activate Account**.
* **Card Controls**: Reset failed PIN attempts, unblock locked cards, block stolen cards.
* **Global Transaction Ledger**: Inspect system-wide ledger entries across all terminals.
* **Financial Reports**: Aggregated metrics for Total Deposits, Total Withdrawals, Bank Total Balance, and Today's Volume with printable layout.

---

## 🎯 OOP Concepts Implementation Matrix

| OOP Principle | File / Class Location | Description |
| :--- | :--- | :--- |
| **Classes & Objects** | [`com.apexbank.model.*`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model) | Concrete models for `Customer`, `Account`, `ATMCard`, `Transaction`, `Receipt`. |
| **Encapsulation** | [`Account.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Account.java) | Private fields; guarded balance mutations via `credit()` and `debit()`. |
| **Abstraction** | [`User.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/User.java), [`Account.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Account.java) | Abstract classes declaring `getDisplayName()`, `validateWithdrawal()`, etc. |
| **Inheritance** | [`SavingsAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/SavingsAccount.java), [`CurrentAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/CurrentAccount.java) | Extends `Account` base class; `Customer` and `Admin` extend `User`. |
| **Polymorphism (Runtime)** | [`SavingsAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/SavingsAccount.java#L35), [`CurrentAccount.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/CurrentAccount.java#L38) | Overrides `validateWithdrawal()`: Savings enforces ₹1k min bal; Current allows overdraft. |
| **Method Overloading** | [`Account.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Account.java#L70), [`SecurityUtil.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/util/SecurityUtil.java) | `deposit(amount)` vs `deposit(amount, note)`; `sha256(data)` vs `hashWithSalt(data, salt)`. |
| **Constructor Overloading** | [`Customer.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Customer.java#L25-L46), [`Transaction.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/model/Transaction.java) | Multiple constructors for minimal vs full profile entity hydration. |
| **Interfaces** | [`AccountOperations.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/AccountOperations.java), [`ATMService.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/ATMService.java) | Segregated contracts separating terminal operations from administrative services. |
| **Exception Handling** | [`com.apexbank.exception.*`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/exception) | Custom exception hierarchy: `InsufficientFundsException`, `DailyLimitExceededException`, etc. |
| **Collections Framework** | [`FullStatementServlet.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/controller/FullStatementServlet.java) | `List<Transaction>`, `Collections.sort()` with `Comparator`, `ConcurrentHashMap` for OTPs. |
| **File / Stream Handling** | [`PDFReceiptGenerator.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/util/PDFReceiptGenerator.java) | Binary PDF output stream generation and classpath resource parsing. |
| **JDBC Connectivity** | [`com.apexbank.dao.*`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/dao) | 100% PreparedStatements (SQL Injection free); multi-statement ACID commit/rollback. |
| **DAO Pattern** | [`CustomerDAO`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/dao/CustomerDAO.java), [`AccountDAO`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/dao/AccountDAO.java), etc. | Decouples database queries from business rules. |
| **Factory Pattern** | [`DAOFactory.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/dao/DAOFactory.java), [`ServiceFactory.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/service/ServiceFactory.java) | Centralized instantiation of components. |
| **Singleton Pattern** | [`DBConnection.java`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/java/com/apexbank/util/DBConnection.java) | Thread-safe database connection provider using double-checked locking. |

---

## 📁 Project Directory Structure

```
bankmanagement/
├── pom.xml                                   # Maven dependencies & build descriptor
├── .project                                  # Eclipse project configuration
├── .classpath                                # Eclipse classpath definitions
├── .settings/                                # Eclipse WST Web Facet configurations
├── database/
│   ├── schema.sql                            # Full DDL schema with tables, constraints, indexes
│   └── seed_data.sql                         # Sample seed data (accounts, cards, transactions, admins)
├── docs/
│   ├── UML_Class_Diagram.md                  # Formal Mermaid UML class diagrams
│   ├── Use_Case_Diagram.md                   # Actor and use case specifications
│   ├── Sequence_Diagram.md                   # Dynamic transaction execution flows
│   ├── ER_Diagram.md                         # Relational database entity diagram
│   ├── Activity_Diagram.md                   # Workflow and decision branch diagrams
│   ├── OOP_Concepts_Showcase.md              # Viva and presentation defense guide
│   └── Deployment_Guide.md                   # Step-by-step Tomcat & MySQL installation
├── src/
│   └── main/
│       ├── java/com/apexbank/
│       │   ├── model/                        # Java Beans & Domain Entities (OOP Hierarchy)
│       │   ├── dao/                          # Data Access Objects (JDBC & PreparedStatements)
│       │   ├── service/                      # Core Business Logic & Limits Validation
│       │   ├── exception/                    # Custom Banking Checked Exceptions
│       │   ├── controller/                   # Base & Feature Servlets (Customer & Admin)
│       │   ├── filter/                       # Security, Encoding & Session Auth Filters
│       │   └── util/                         # Crypto, Masking, Currency & PDF Generator
│       ├── resources/
│       │   └── db.properties                 # MySQL connection strings & ATM settings
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml                   # Deployment Descriptor (Filters, Sessions, Errors)
│           │   └── views/
│           │       ├── common/               # Shared JSP headers, navbars, messages, footers
│           │       ├── atm/                  # Customer ATM screens (Dashboard, Kiosk, Receipts)
│           │       ├── admin/                # Admin Console (Customers, Accounts, Reports)
│           │       ├── auth/                 # Login & Forgot PIN (OTP simulation)
│           │       └── error/                # 404 & 500 error pages
│           ├── assets/
│           │   ├── css/                      # Theme, ATM Kiosk bezel, Thermal Receipt CSS
│           │   ├── js/                       # Audio synthesis, keypad, theme toggle, print
│           │   └── img/                      # Bank Logo & Graphic SVGs
│           └── index.jsp                     # Public landing page with test credentials
└── README.md
```

---

## ⚡ Quick Start & Deployment

### 1. Database Setup
Execute the scripts in your MySQL client:
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed_data.sql
```
Update database credentials in [`src/main/resources/db.properties`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/resources/db.properties) if your MySQL password differs from `root`.

### 2. Run in Eclipse or IntelliJ
* **Eclipse**: File &rarr; Import... &rarr; Existing Maven Projects &rarr; Select `bankmanagement` &rarr; Add to Tomcat Server &rarr; Run.
* **IntelliJ**: Open &rarr; Select `bankmanagement` &rarr; Run on local Tomcat Server configuration.

### 3. Access Web Application
Open your browser and navigate to:
```
http://localhost:8080/bankmanagement/
```

---

## 🔑 Pre-Configured Test Credentials

| Portal | User / Name | Identifier / Card Number | PIN / Password | Type / Balance |
| :--- | :--- | :--- | :--- | :--- |
| **ATM Customer** | **John Doe** | `4532 7890 1234 5678` | `1234` | **Savings (₹58,450.00)** |
| **ATM Customer** | **Priya Sharma** | `5241 8901 2345 6789` | `1234` | **Current (₹1,25,000.00, ₹25k Overdraft)** |
| **ATM Customer** | **Rahul Verma** | `6071 8901 2345 6789` | `1234` | **Savings (₹12,800.00)** |
| **Admin Console** | **Vikram Malhotra** | `admin` | `admin123` | **Super Administrator** |

---

## 🎓 Academic Defense & Evaluation Questions (Viva Prep)

1. **Why avoid Spring Boot in a Final Year Project?**
   * *Answer*: Spring Boot abstracts away core Java fundamentals behind annotations (`@Autowired`, `@RestController`). Writing native Servlets, custom DAOs, and manual JDBC transaction demarcation proves understanding of JVM lifecycle, memory model, and pure OOP design patterns.
2. **How is Polymorphism demonstrated in balance validation?**
   * *Answer*: `SavingsAccount` and `CurrentAccount` override `validateWithdrawal(amount)`. Savings enforces a ₹1,000 minimum balance, while Current allows a ₹25,000 overdraft. The calling service simply calls `account.validateWithdrawal(amount)`, and Java resolves the correct behavior dynamically at runtime.
3. **How does the system ensure ACID compliance during money transfers?**
   * *Answer*: In `ATMServiceImpl.transfer()`, auto-commit is disabled (`conn.setAutoCommit(false)`). The sender account debit, recipient credit, and transaction log inserts are performed in sequence. If any step fails, `conn.rollback()` restores the initial ledger state.
4. **How does the card lockout mechanism work?**
   * *Answer*: Every failed PIN attempt triggers `cardDAO.incrementFailedAttempts()`. When the counter reaches 3, the card status updates to `LOCKED` in MySQL and further attempts throw `CardBlockedException` until reset by an admin or verified via SMS OTP.
