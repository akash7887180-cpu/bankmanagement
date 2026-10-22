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
import java.util.List;

/**
 * Controller for ATM Mini Statement (Last 10 Transactions).
 */
@WebServlet(name = "MiniStatementServlet", urlPatterns = {"/atm/ministatement"})
public class MiniStatementServlet extends BaseServlet {
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
            List<Transaction> transactions = atmService.getMiniStatement(card.getCardNumber());

            req.setAttribute("transactions", transactions);
            req.setAttribute("accountNumber", account.getAccountNumber());
            req.setAttribute("maskedAccount", MaskingUtil.maskAccountNumber(account.getAccountNumber()));
            req.setAttribute("customerName", customer.getFullName());
            req.setAttribute("currentBalance", CurrencyUtil.format(account.getBalance()));

            forward(req, resp, "atm/ministatement");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading mini statement: " + e.getMessage());
            forward(req, resp, "atm/ministatement");
        }
    }
}
