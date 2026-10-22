# Deployment & Setup Guide
## Apex ATM Banking Management System

Follow this step-by-step guide to configure the database, import the project into **Eclipse IDE** or **IntelliJ IDEA**, deploy onto **Apache Tomcat**, and run the application.

---

### 1. Prerequisites

| Software | Recommended Version | Purpose |
| :--- | :--- | :--- |
| **Java Development Kit (JDK)** | Java 8, 11, or 17 | Core Runtime & Compiler |
| **Apache Tomcat** | Tomcat 8.5 or 9.0 (Tomcat 10+ supported) | Servlet & JSP Container |
| **MySQL Server** | MySQL 5.7 or 8.0+ | Relational Database Engine |
| **IDE** | Eclipse IDE for Enterprise Java / IntelliJ IDEA Ultimate or Community | Development & Debugging |
| **Maven** (Optional) | Maven 3.6+ | Automated Dependency Management |

---

### 2. Database Initialization (MySQL)

1. Open **MySQL Workbench**, **DBeaver**, or **Command Prompt / Terminal**:
   ```bash
   mysql -u root -p
   ```
2. Execute the schema creation script:
   ```sql
   SOURCE c:/Users/Akash/Downloads/bankmanagement/database/schema.sql;
   ```
3. Execute the seed data script:
   ```sql
   SOURCE c:/Users/Akash/Downloads/bankmanagement/database/seed_data.sql;
   ```
4. Verify the database and tables:
   ```sql
   USE apex_bank_db;
   SHOW TABLES;
   SELECT * FROM customers;
   SELECT * FROM accounts;
   SELECT * FROM atm_cards;
   ```

---

### 3. Database Credentials Configuration

Open the properties configuration file located at:
[`src/main/resources/db.properties`](file:///c:/Users/Akash/Downloads/bankmanagement/src/main/resources/db.properties)

Ensure the credentials match your local MySQL installation:
```properties
db.url=jdbc:mysql://localhost:3306/apex_bank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.username=root
db.password=root
```
*(Change `db.password` to match your local MySQL root password if different).*

---

### 4. Running in Eclipse IDE

1. **Open Eclipse IDE for Enterprise Java and Web Developers**.
2. Go to **File** &rarr; **Import...**
3. Select **Maven** &rarr; **Existing Maven Projects** (or **General** &rarr; **Existing Projects into Workspace**), click **Next**.
4. Browse to the project root directory: `c:\Users\Akash\Downloads\bankmanagement`.
5. Click **Finish**. Eclipse will import and configure the project facets automatically.
6. **Configure Tomcat Server**:
   - Open the **Servers** tab (Window &rarr; Show View &rarr; Servers).
   - Right-click &rarr; **New** &rarr; **Server**.
   - Select **Apache** &rarr; **Tomcat v9.0 Server** (or v8.5), click **Next**.
   - Browse to your local Apache Tomcat installation directory.
   - In **Add and Remove...**, select **bankmanagement**, click **Add &gt;**, then **Finish**.
7. **Start Server**:
   - Right-click the configured Tomcat server &rarr; **Start**.
8. **Access in Browser**:
   - Open: `http://localhost:8080/bankmanagement/`

---

### 5. Running in IntelliJ IDEA

1. **Open IntelliJ IDEA**.
2. Select **File** &rarr; **Open...** and select `c:\Users\Akash\Downloads\bankmanagement`.
3. If prompted, open as a **Maven Project**.
4. **Configure Tomcat**:
   - Go to **Run** &rarr; **Edit Configurations...**
   - Click the **+** button &rarr; **Tomcat Server** &rarr; **Local**.
   - Click **Configure...** and set the path to your Tomcat installation.
   - In the **Deployment** tab, click **+** &rarr; **Artifact...** and select `bankmanagement:war exploded`.
   - Set **Application context** to: `/bankmanagement`
5. Click **Run** or **Debug**.
6. Access in Browser: `http://localhost:8080/bankmanagement/`

---

### 6. Command Line Build (Maven `.war` Deployment)

If you have Maven installed on your machine, you can generate a standalone `.war` distribution with a single command:
```bash
mvn clean package
```
This produces: `target/bankmanagement.war`.
Simply copy `bankmanagement.war` into your Tomcat `webapps/` folder and start Tomcat using `startup.bat` or `startup.sh`.

---

### 7. Pre-Configured Test Credentials

#### Customer ATM Accounts
| Customer Name | Account Type | 16-Digit Card Number | PIN | Initial Balance |
| :--- | :--- | :--- | :--- | :--- |
| **John Doe** | Savings Account | `4532 7890 1234 5678` | `1234` | **₹ 58,450.00** |
| **Priya Sharma** | Current Account (₹25k Overdraft) | `5241 8901 2345 6789` | `1234` | **₹ 1,25,000.00** |
| **Rahul Verma** | Savings Account | `6071 8901 2345 6789` | `1234` | **₹ 12,800.00** |
| **Ananya Iyer** | Savings Account | `4111 2222 3333 4444` | `1234` | **₹ 95,200.00** |

#### Bank Administrator Console
- **URL**: `http://localhost:8080/bankmanagement/admin/login`
- **Username**: `admin`
- **Password**: `admin123`
