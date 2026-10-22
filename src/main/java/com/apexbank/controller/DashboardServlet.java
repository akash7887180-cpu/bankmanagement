package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.model.Transaction;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.CurrencyUtil;
import com.apexbank.util.MaskingUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller for the modern customer ATM / Internet Banking Dashboard.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/atm/dashboard"})
public class DashboardServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);

        ATMCard card = getSessionCard(req);
        Account account = getSessionAccount(req);
        Customer customer = getSessionCustomer(req);

        if (card == null || account == null) {
            redirect(resp, req.getContextPath() + "/login");
            return;
        }

        try {
            double remainingLimit = atmService.getRemainingDailyLimit(card.getCardNumber());
            List<Transaction> recentTransactions = atmService.getMiniStatement(card.getCardNumber());

            req.setAttribute("customerName", customer != null ? customer.getFullName() : "Valued Customer");
            req.setAttribute("accountNumber", account.getAccountNumber());
            req.setAttribute("maskedAccount", MaskingUtil.maskAccountNumber(account.getAccountNumber()));
            req.setAttribute("maskedCard", card.getMaskedCardNumber());
            req.setAttribute("availableBalance", CurrencyUtil.format(account.getBalance()));
            req.setAttribute("rawBalance", account.getBalance());
            req.setAttribute("accountType", account.getAccountType());
            req.setAttribute("dailyLimitRemaining", CurrencyUtil.format(remainingLimit));
            req.setAttribute("cardType", card.getCardType().getDisplayName());

            // Current date and time
            String currentDateTime = new SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm:ss a").format(new Date());
            req.setAttribute("currentDateTime", currentDateTime);

            // Last login from session
            Object loginTime = req.getSession().getAttribute("SESSION_LOGIN_TIME");
            req.setAttribute("lastLogin", loginTime != null ? loginTime.toString() : currentDateTime);

            req.setAttribute("recentTransactions", recentTransactions);

            forward(req, resp, "atm/dashboard");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            forward(req, resp, "atm/dashboard");
        }
    }
}
