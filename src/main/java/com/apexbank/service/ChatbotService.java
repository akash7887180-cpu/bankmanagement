package com.apexbank.service;

import com.apexbank.dao.AccountDAO;
import com.apexbank.dao.DAOFactory;
import com.apexbank.dao.TransactionDAO;
import com.apexbank.model.*;
import com.apexbank.util.CurrencyUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intelligent Customer Service & Problem-Solving Chatbot Engine for Apex Bank.
 * Handles Natural Language understanding, diagnostic troubleshooting, loan calculations,
 * and automated banking assistance.
 */
public class ChatbotService {

    private final LoanService loanService = new LoanServiceImpl();
    private final SupportService supportService = new SupportServiceImpl();
    private final AccountDAO accountDAO = DAOFactory.getAccountDAO();
    private final TransactionDAO transactionDAO = DAOFactory.getTransactionDAO();

    public static class ChatResponse {
        private String message;
        private List<String> suggestions;
        private String actionUrl;
        private String actionText;
        private String actionClass;

        public ChatResponse(String message) {
            this.message = message;
            this.suggestions = new ArrayList<>();
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public List<String> getSuggestions() { return suggestions; }
        public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

        public String getActionUrl() { return actionUrl; }
        public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }

        public String getActionText() { return actionText; }
        public void setActionText(String actionText) { this.actionText = actionText; }

        public String getActionClass() { return actionClass; }
        public void setActionClass(String actionClass) { this.actionClass = actionClass; }
    }

    public ChatResponse processMessage(String userInput, Customer customer, Account account, ATMCard card) {
        if (userInput == null || userInput.trim().isEmpty()) {
            ChatResponse r = new ChatResponse("Hello! I am <strong>Apex Assistant</strong>, your 24x7 customer service representative. How can I help resolve your banking or loan inquiry today?");
            addDefaultSuggestions(r);
            return r;
        }

        String text = userInput.trim().toLowerCase();

        // 1. GREETINGS
        if (text.matches("^(hi|hello|hey|good morning|good afternoon|good evening|namaste).*")) {
            String name = (customer != null) ? " " + customer.getFirstName() : "";
            ChatResponse r = new ChatResponse("Hello" + name + "! 👋 Welcome to <strong>Apex National Bank Helpdesk</strong>. I am trained to solve financial issues, assist with Money & Gold Loans, and protect your accounts. How can I assist you right now?");
            addDefaultSuggestions(r);
            return r;
        }

        // 2. PROBLEM SOLVING: FAILED ATM CASH DISPENSE (Cash not dispensed but debited)
        if (text.contains("not dispense") || text.contains("didn't dispense") || text.contains("no cash") ||
            text.contains("cash deducted") || text.contains("money deducted") || text.contains("money debited") ||
            text.contains("failed atm") || text.contains("atm error") || text.contains("cash not given")) {

            StringBuilder sb = new StringBuilder();
            sb.append("<div class='chat-alert alert-warning mb-2'>");
            sb.append("<strong>⚠️ ATM Cash Dispense Problem Solving Guide:</strong><br>");
            sb.append("Don't worry! If money was deducted from your account but the ATM failed to dispense cash:");
            sb.append("<ol class='mt-2 mb-2 ps-3'>");
            sb.append("<li><strong>Auto-Reversal:</strong> Most failed transactions auto-reverse within 24 to 48 hours per RBI guidelines.</li>");
            sb.append("<li><strong>Compensation:</strong> If not reversed within 5 working days, you are entitled to ₹100/day delay compensation.</li>");
            sb.append("<li><strong>Immediate Dispute:</strong> We can file an official investigation ticket right now with your ATM terminal details.</li>");
            sb.append("</ol>");
            sb.append("</div>");

            ChatResponse r = new ChatResponse(sb.toString());
            r.setActionUrl("/atm/support?type=FAILED_ATM_CASH");
            r.setActionText("📝 File ATM Cash Dispute Ticket");
            r.setActionClass("btn-danger");
            r.getSuggestions().add("Check recent transactions");
            r.getSuggestions().add("Talk to customer care");
            r.getSuggestions().add("Check account balance");
            return r;
        }

        // 3. PROBLEM SOLVING: LOST OR STOLEN CARD / EMERGENCY BLOCK
        if ((text.contains("card") && (text.contains("lost") || text.contains("block") || text.contains("lock") || text.contains("freeze") || text.contains("missing"))) ||
            text.contains("lost card") || text.contains("stolen") || text.contains("block card") ||
            text.contains("lock card") || text.contains("freeze card") || text.contains("card missing") ||
            text.contains("unauthorized") || text.contains("fraud") || text.contains("hacked")) {

            StringBuilder sb = new StringBuilder();
            sb.append("<div class='chat-alert alert-danger mb-2'>");
            sb.append("<strong>🚨 Emergency Security Protection Protocol:</strong><br>");
            if (card != null) {
                sb.append("Your active card ending in <strong>").append(card.getMaskedCardNumber()).append("</strong> can be locked immediately to stop all unauthorized withdrawals.");
            } else {
                sb.append("You can immediately lock your ATM card to prevent any unauthorized debits or ATM cash withdrawals.");
            }
            sb.append("<p class='mt-2 mb-0 small'>Once locked, nobody can withdraw funds until you unlock it via verified OTP.</p>");
            sb.append("</div>");

            ChatResponse r = new ChatResponse(sb.toString());
            r.setActionUrl("/atm/support?type=LOST_CARD_FRAUD");
            r.setActionText("🔒 Emergency Lock ATM Card Now");
            r.setActionClass("btn-danger");
            r.getSuggestions().add("How to unlock card later?");
            r.getSuggestions().add("Report unauthorized debit");
            r.getSuggestions().add("Customer care number");
            return r;
        }

        // 4. PROBLEM SOLVING: ACCOUNT LOCKED / PIN BLOCKED / FORGOT PIN
        if (text.contains("pin block") || text.contains("card block") || text.contains("forgot pin") ||
            text.contains("reset pin") || text.contains("change pin") || text.contains("locked")) {

            StringBuilder sb = new StringBuilder();
            sb.append("<strong>🔐 ATM PIN & Lock Troubleshooting:</strong><br>");
            sb.append("For your security, Apex Bank locks cards automatically after <strong>3 incorrect PIN attempts</strong>.<br>");
            sb.append("<ul class='mt-2 mb-2 ps-3'>");
            sb.append("<li><strong>Reset via OTP:</strong> You can verify your registered mobile number to reset your PIN instantly online.</li>");
            sb.append("<li><strong>Change PIN:</strong> If you remember your old PIN, use the ATM 'Change PIN' menu anytime.</li>");
            sb.append("</ul>");

            ChatResponse r = new ChatResponse(sb.toString());
            r.setActionUrl("/forgot-pin");
            r.setActionText("🔑 Unlock Card / Reset PIN with OTP");
            r.setActionClass("btn-primary");
            r.getSuggestions().add("How to change PIN?");
            r.getSuggestions().add("Contact customer care");
            return r;
        }

        // 5. PROBLEM SOLVING: FINANCIAL HARDSHIP / EMI ISSUE / DISPUTES
        if (text.contains("financial issue") || text.contains("financial problem") || text.contains("hardship") ||
            text.contains("cannot pay emi") || text.contains("emi delay") || text.contains("reschedule") ||
            text.contains("grievance") || text.contains("complaint") || text.contains("dispute")) {

            StringBuilder sb = new StringBuilder();
            sb.append("<strong>🤝 Apex Financial Assistance & Grievance Redressal:</strong><br>");
            sb.append("We understand that financial difficulties or unexpected banking charges happen. Here is how Apex Bank can assist you:");
            sb.append("<ul class='mt-2 mb-2 ps-3'>");
            sb.append("<li><strong>EMI Restructuring:</strong> Apply to extend your loan tenure to reduce monthly payments.</li>");
            sb.append("<li><strong>Transaction Dispute:</strong> Contest an erroneous bank charge or failed transfer.</li>");
            sb.append("<li><strong>Priority Officer:</strong> A dedicated relationship officer will review your ticket within 24 hours.</li>");
            sb.append("</ul>");

            ChatResponse r = new ChatResponse(sb.toString());
            r.setActionUrl("/atm/support");
            r.setActionText("📝 Submit Grievance / Assistance Ticket");
            r.setActionClass("btn-warning");
            r.getSuggestions().add("Money Loan options");
            r.getSuggestions().add("Gold Loan for instant cash");
            r.getSuggestions().add("Check account balance");
            return r;
        }

        // 6. GOLD LOAN INQUIRY & CALCULATOR
        if (text.contains("gold loan") || text.contains("gold rate") || text.contains("gold per gram") ||
            text.contains("pawn gold") || text.contains("gold jewel") || text.contains("gold")) {

            // Check if user entered gold weight in grams (e.g. "50g", "25 grams", "10 gram")
            Pattern p = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(g|gm|gms|gram|grams)?");
            Matcher m = p.matcher(text);
            BigDecimal grams = null;
            while (m.find()) {
                try {
                    double val = Double.parseDouble(m.group(1));
                    if (val > 0 && val < 5000) {
                        grams = BigDecimal.valueOf(val);
                        break;
                    }
                } catch (Exception ignored) {}
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<strong>🪙 Apex Instant Gold Loan (Up to 75% LTV):</strong><br>");
            sb.append("Pledge your gold ornaments for instant cash disbursement directly into your bank account!<br>");
            sb.append("<div class='p-2 my-2 bg-dark rounded border border-warning small'>");
            sb.append("<strong>Today's Benchmark Gold Rates:</strong><br>");
            sb.append("• <strong>24 Karat (Pure):</strong> ₹7,500 / gram<br>");
            sb.append("• <strong>22 Karat (Jewelry):</strong> ₹6,875 / gram (Standard)<br>");
            sb.append("• <strong>18 Karat:</strong> ₹5,625 / gram<br>");
            sb.append("• <strong>Interest Rate:</strong> Just <strong>0.75% per month</strong> (9.0% p.a.)");
            sb.append("</div>");

            if (grams != null) {
                BigDecimal eligible22k = loanService.calculateMaxGoldLoan(grams, 22);
                BigDecimal monthlyEmi = Loan.calculateEmi(eligible22k, new BigDecimal("9.00"), 12);
                sb.append("<div class='alert alert-success p-2 small'>");
                sb.append("<strong>✨ Valuation for ").append(grams).append(" grams (22K Gold):</strong><br>");
                sb.append("• Gold Market Value: <strong>").append(CurrencyUtil.formatINR(grams.multiply(new BigDecimal("6875")))).append("</strong><br>");
                sb.append("• <strong>Eligible Cash Loan (75%):</strong> <span class='text-success fw-bold fs-6'>").append(CurrencyUtil.formatINR(eligible22k)).append("</span><br>");
                sb.append("• Est. 12-Month EMI: <strong>").append(CurrencyUtil.formatINR(monthlyEmi)).append("/mo</strong>");
                sb.append("</div>");
            } else {
                sb.append("<em>Tip: You can ask me <strong>'Gold loan for 40 grams'</strong> to calculate your exact eligible cash amount!</em>");
            }

            ChatResponse r = new ChatResponse(sb.toString());
            r.setActionUrl("/atm/loans#gold");
            r.setActionText("🪙 Apply for Instant Gold Loan");
            r.setActionClass("btn-warning");
            r.getSuggestions().add("Gold loan for 20 grams");
            r.getSuggestions().add("Gold loan for 50 grams");
            r.getSuggestions().add("Money Loan options");
            r.getSuggestions().add("Required gold documents");
            return r;
        }

        // 7. MONEY LOAN INQUIRY & CALCULATOR (Personal, Home, Vehicle, Business)
        if (text.contains("money loan") || text.contains("personal loan") || text.contains("home loan") ||
            text.contains("vehicle loan") || text.contains("business loan") || text.contains("loan") ||
            text.contains("emi") || text.contains("borrow") || text.contains("credit loan")) {

            // Try to extract loan amount from query (e.g. "500000", "5 lakhs", "200000")
            Pattern p = Pattern.compile("(\\d{4,8})");
            Matcher m = p.matcher(text);
            BigDecimal amount = null;
            if (m.find()) {
                try {
                    amount = new BigDecimal(m.group(1));
                } catch (Exception ignored) {}
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<strong>💰 Apex Comprehensive Money Loan Suite:</strong><br>");
            sb.append("Choose from low-interest loans tailored to your needs with paperless approval:");
            sb.append("<ul class='mt-1 mb-2 ps-3 small'>");
            sb.append("<li><strong>Personal Loan:</strong> 10.5% p.a. | Up to ₹15 Lakhs (Quick Disbursal)</li>");
            sb.append("<li><strong>Home Loan:</strong> 8.4% p.a. | Up to ₹1 Crore (Low Processing Fee)</li>");
            sb.append("<li><strong>Vehicle Loan:</strong> 9.0% p.a. | 100% on-road financing</li>");
            sb.append("<li><strong>Business Loan:</strong> 12.0% p.a. | Working capital & expansion</li>");
            sb.append("</ul>");

            if (amount != null && amount.compareTo(new BigDecimal("10000")) >= 0) {
                BigDecimal emi3yr = Loan.calculateEmi(amount, new BigDecimal("10.50"), 36);
                BigDecimal emi5yr = Loan.calculateEmi(amount, new BigDecimal("10.50"), 60);
                sb.append("<div class='alert alert-info p-2 small'>");
                sb.append("<strong>📊 EMI Estimate for ").append(CurrencyUtil.formatINR(amount)).append(" @ 10.5%:</strong><br>");
                sb.append("• <strong>3 Years (36 mos):</strong> ").append(CurrencyUtil.formatINR(emi3yr)).append("/month<br>");
                sb.append("• <strong>5 Years (60 mos):</strong> ").append(CurrencyUtil.formatINR(emi5yr)).append("/month");
                sb.append("</div>");
            } else {
                sb.append("<em>Tip: Ask me <strong>'Calculate loan for 300000'</strong> to see instant monthly EMI breakdowns!</em>");
            }

            ChatResponse r = new ChatResponse(sb.toString());
            r.setActionUrl("/atm/loans#money");
            r.setActionText("💰 Calculate & Apply for Money Loan");
            r.setActionClass("btn-primary");
            r.getSuggestions().add("Calculate loan for 500000");
            r.getSuggestions().add("Calculate loan for 200000");
            r.getSuggestions().add("Gold Loan rates");
            r.getSuggestions().add("Eligibility criteria");
            return r;
        }

        // 8. BALANCE ENQUIRY
        if (text.contains("balance") || text.contains("how much money") || text.contains("account balance") ||
            text.contains("available balance") || text.contains("check balance")) {

            if (account != null) {
                // Re-fetch latest balance from DB
                Account freshAcc = accountDAO.findById(account.getAccountId());
                double bal = freshAcc != null ? freshAcc.getBalance() : account.getBalance();
                String accNum = freshAcc != null ? freshAcc.getMaskedAccountNumber() : account.getAccountNumber();

                String msg = "<strong>💳 Account Balance:</strong><br>" +
                        "Account: <code>" + accNum + "</code> (" + account.getAccountType() + ")<br>" +
                        "Available Balance: <strong class='text-success fs-5'>" + CurrencyUtil.formatINR(bal) + "</strong><br>" +
                        "<span class='text-muted small'>Live verified on core banking ledger.</span>";

                ChatResponse r = new ChatResponse(msg);
                r.setActionUrl("/atm/statement");
                r.setActionText("📄 View Full Statement");
                r.setActionClass("btn-outline-primary");
                r.getSuggestions().add("Recent transactions");
                r.getSuggestions().add("Withdraw Cash");
                r.getSuggestions().add("Apply for Money Loan");
                return r;
            } else {
                ChatResponse r = new ChatResponse("To view your private account balance, please log in with your ATM card and PIN.");
                r.setActionUrl("/login");
                r.setActionText("🔑 Insert Card & Login");
                r.setActionClass("btn-primary");
                return r;
            }
        }

        // 9. MINI STATEMENT / RECENT TRANSACTIONS
        if (text.contains("statement") || text.contains("transaction") || text.contains("recent debit") ||
            text.contains("history") || text.contains("last transaction")) {

            if (account != null) {
                List<Transaction> txns = transactionDAO.findLastTransactions(account.getAccountId(), 3);
                StringBuilder sb = new StringBuilder();
                sb.append("<strong>📑 Recent Account Transactions:</strong><br>");
                if (txns != null && !txns.isEmpty()) {
                    sb.append("<div class='list-group list-group-flush my-2'>");
                    for (Transaction t : txns) {
                        sb.append("<div class='list-group-item bg-transparent px-1 py-1 border-bottom small'>");
                        sb.append("<div class='d-flex justify-content-between'>");
                        sb.append("<span>").append(t.getTransactionType()).append("</span>");
                        sb.append("<strong class='").append(t.getTransactionType() == TransactionType.DEPOSIT ? "text-success" : "text-danger").append("'>");
                        sb.append(t.getTransactionType() == TransactionType.DEPOSIT ? "+" : "-").append(CurrencyUtil.formatINR(t.getAmount()));
                        sb.append("</strong></div>");
                        sb.append("<div class='text-muted' style='font-size:0.75rem;'>").append(t.getReferenceNumber()).append("</div>");
                        sb.append("</div>");
                    }
                    sb.append("</div>");
                } else {
                    sb.append("<p class='text-muted small'>No recent transactions recorded yet.</p>");
                }

                ChatResponse r = new ChatResponse(sb.toString());
                r.setActionUrl("/atm/statement");
                r.setActionText("📄 Download Full Statement");
                r.setActionClass("btn-primary");
                r.getSuggestions().add("Dispute a transaction");
                r.getSuggestions().add("Check balance");
                return r;
            } else {
                ChatResponse r = new ChatResponse("Please insert your ATM card to securely check recent transaction records.");
                r.setActionUrl("/login");
                r.setActionText("🔑 Login to View");
                r.setActionClass("btn-primary");
                return r;
            }
        }

        // 10. CUSTOMER CARE / CONTACT / HELPLINE
        if (text.contains("customer care") || text.contains("phone number") || text.contains("helpline") ||
            text.contains("call") || text.contains("contact") || text.contains("human") || text.contains("support")) {

            String msg = "<strong>📞 Apex 24x7 Customer Helpline:</strong><br>" +
                    "• Toll-Free National Helpline: <strong>1800-420-APEX (2739)</strong><br>" +
                    "• Emergency Fraud Hotlist: <strong>1800-420-9999</strong><br>" +
                    "• Support Email: <strong>support@apexbank.com</strong><br>" +
                    "• Registered Branch: <em>Connaught Place Financial Center, New Delhi</em><br>" +
                    "<span class='text-muted small'>Our support executive queue is open 24x7. You can also file a direct problem-solving ticket below.</span>";

            ChatResponse r = new ChatResponse(msg);
            r.setActionUrl("/atm/support");
            r.setActionText("📝 Open Helpdesk Ticket");
            r.setActionClass("btn-primary");
            addDefaultSuggestions(r);
            return r;
        }

        // 11. FALLBACK / INTELLIGENT DEFAULT
        StringBuilder sb = new StringBuilder();
        sb.append("I understand you are asking about: <em>\"").append(escapeHtml(userInput)).append("\"</em>.<br>");
        sb.append("As your Apex Customer Service Assistant, I can help resolve:");
        sb.append("<ul class='mt-1 mb-2 ps-3 small'>");
        sb.append("<li><strong>Money Loans & EMI:</strong> Personal, Home, and Business loans with instant calculators.</li>");
        sb.append("<li><strong>Gold Loans:</strong> Instant cash against gold ornaments at 0.75%/month with live gold rates.</li>");
        sb.append("<li><strong>Financial Problems:</strong> Failed ATM dispense disputes, emergency card locking, and grievance tickets.</li>");
        sb.append("<li><strong>Account Operations:</strong> Balance inquiries, statements, and PIN unlock.</li>");
        sb.append("</ul>");

        ChatResponse r = new ChatResponse(sb.toString());
        r.setActionUrl("/atm/support");
        r.setActionText("📝 Contact Helpdesk Specialist");
        r.setActionClass("btn-outline-primary");
        addDefaultSuggestions(r);
        return r;
    }

    private void addDefaultSuggestions(ChatResponse r) {
        r.getSuggestions().add("💰 Money Loan EMI");
        r.getSuggestions().add("🪙 Gold Loan Rates");
        r.getSuggestions().add("⚠️ ATM Cash Not Dispensed");
        r.getSuggestions().add("🚨 Emergency Block Card");
        r.getSuggestions().add("💳 Check Balance");
    }

    private String escapeHtml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
