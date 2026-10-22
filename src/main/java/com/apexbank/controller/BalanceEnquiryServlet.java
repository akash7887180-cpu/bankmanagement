package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.CurrencyUtil;
import com.apexbank.util.MaskingUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for ATM Balance Enquiry.
 */
@WebServlet(name = "BalanceEnquiryServlet", urlPatterns = {"/atm/balance"})
public class BalanceEnquiryServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);

        ATMCard card = getSessionCard(req);
        Account account = getSessionAccount(req);
        Customer customer = getSessionCustomer(req);

        try {
            double balance = atmService.getBalance(card.getCardNumber());
            double remainingLimit = atmService.getRemainingDailyLimit(card.getCardNumber());

            req.setAttribute("accountNumber", account.getAccountNumber());
            req.setAttribute("maskedAccount", MaskingUtil.maskAccountNumber(account.getAccountNumber()));
            req.setAttribute("customerName", customer.getFullName());
            req.setAttribute("currentBalance", CurrencyUtil.format(balance));
            req.setAttribute("balanceInWords", CurrencyUtil.convertToWords(balance));
            req.setAttribute("minimumBalance", CurrencyUtil.format(account.getMinimumBalance()));
            req.setAttribute("accountType", account.getAccountType());
            req.setAttribute("dailyLimitRemaining", CurrencyUtil.format(remainingLimit));

            forward(req, resp, "atm/balance");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error fetching balance: " + e.getMessage());
            forward(req, resp, "atm/balance");
        }
    }
}
