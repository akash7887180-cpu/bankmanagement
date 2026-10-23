<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*, java.io.*, com.apexbank.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <title>Apex Bank - Cloud Health & Diagnostics</title>
    <style>
        body { font-family: monospace; padding: 20px; background: #0f172a; color: #f8fafc; }
        .card { background: #1e293b; padding: 15px; border-radius: 8px; margin-bottom: 15px; border: 1px solid #334155; }
        .ok { color: #4ade80; font-weight: bold; }
        .error { color: #f87171; font-weight: bold; }
        h2 { margin-top: 0; color: #38bdf8; }
        table { border-collapse: collapse; width: 100%; margin-top: 10px; }
        th, td { border: 1px solid #334155; padding: 6px 10px; text-align: left; }
        th { background: #0f172a; }
    </style>
</head>
<body>
    <h2>🚀 Apex Bank Container Diagnostics</h2>
    
    <div class="card">
        <h3>System Info</h3>
        <p>Build Version: <strong>2026-10-23.MariaDB-v2</strong></p>
        <p>Server Time: <strong><%= new java.util.Date() %></strong></p>
        <p>Java: <strong><%= System.getProperty("java.version") %> (<%= System.getProperty("java.vendor") %>)</strong></p>
        <p>OS: <strong><%= System.getProperty("os.name") %> <%= System.getProperty("os.version") %> (<%= System.getProperty("os.arch") %>)</strong></p>
        <p>Memory Free/Total/Max: <strong><%= Runtime.getRuntime().freeMemory() / 1024 / 1024 %>MB / <%= Runtime.getRuntime().totalMemory() / 1024 / 1024 %>MB / <%= Runtime.getRuntime().maxMemory() / 1024 / 1024 %>MB</strong></p>
    </div>

    <div class="card">
        <h3>Database Connection Check</h3>
        <%
            String dbStatus = "UNKNOWN";
            String dbDetails = "";
            int custCount = -1, accCount = -1, cardCount = -1;
            List<String> cardList = new ArrayList<String>();
            try (Connection conn = DBConnection.getInstance().getConnection()) {
                if (conn != null && !conn.isClosed()) {
                    dbStatus = "CONNECTED";
                    DatabaseMetaData meta = conn.getMetaData();
                    dbDetails = meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion() + " via " + meta.getDriverName();
                    
                    try (Statement stmt = conn.createStatement()) {
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM customers");
                        if (rs.next()) custCount = rs.getInt(1);
                        
                        rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts");
                        if (rs.next()) accCount = rs.getInt(1);
                        
                        rs = stmt.executeQuery("SELECT COUNT(*) FROM atm_cards");
                        if (rs.next()) cardCount = rs.getInt(1);
                        
                        rs = stmt.executeQuery("SELECT c.card_number, c.pin_plain, cust.first_name, a.balance FROM atm_cards c JOIN customers cust ON c.customer_id = cust.customer_id JOIN accounts a ON c.account_id = a.account_id");
                        while (rs.next()) {
                            cardList.add(rs.getString("first_name") + " | Card: " + rs.getString("card_number") + " | PIN: " + rs.getString("pin_plain") + " | Balance: ₹" + rs.getString("balance"));
                        }
                    }
                }
            } catch (Throwable t) {
                dbStatus = "ERROR";
                StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                dbDetails = sw.toString();
            }
        %>
        
        <p>Status: <span class="<%= "CONNECTED".equals(dbStatus) ? "ok" : "error" %>"><%= dbStatus %></span></p>
        <% if ("CONNECTED".equals(dbStatus)) { %>
            <p>Database Product: <strong><%= dbDetails %></strong></p>
            <p>Customers: <strong><%= custCount %></strong> | Accounts: <strong><%= accCount %></strong> | ATM Cards: <strong><%= cardCount %></strong></p>
            <table>
                <thead>
                    <tr><th>Customer Cards Seeded</th></tr>
                </thead>
                <tbody>
                    <% for (String c : cardList) { %>
                        <tr><td><%= c %></td></tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <pre class="error"><%= dbDetails %></pre>
        <% } %>
    </div>
</body>
</html>
