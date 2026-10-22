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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controller for Full Statement with Search, Filter, Sort, and Export.
 */
@WebServlet(name = "FullStatementServlet", urlPatterns = {"/atm/statement"})
public class FullStatementServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);

        ATMCard card = getSessionCard(req);
        Account account = getSessionAccount(req);
        Customer customer = getSessionCustomer(req);

        String type = req.getParameter("type");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String search = req.getParameter("search");
        String sort = req.getParameter("sort"); // date_desc, date_asc, amount_desc, amount_asc

        try {
            List<Transaction> transactions = atmService.getFullStatement(card.getCardNumber(), type, startDate, endDate, search);

            // Sorting logic showcasing Collections Framework
            if ("date_asc".equalsIgnoreCase(sort)) {
                transactions.sort(Comparator.comparing(Transaction::getCreatedAt));
            } else if ("amount_desc".equalsIgnoreCase(sort)) {
                transactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
            } else if ("amount_asc".equalsIgnoreCase(sort)) {
                transactions.sort(Comparator.comparing(Transaction::getAmount));
            } else {
                // Default date_desc
                transactions.sort(Comparator.comparing(Transaction::getCreatedAt).reversed());
            }

            req.setAttribute("transactions", transactions);
            req.setAttribute("accountNumber", account.getAccountNumber());
            req.setAttribute("maskedAccount", MaskingUtil.maskAccountNumber(account.getAccountNumber()));
            req.setAttribute("customerName", customer.getFullName());
            req.setAttribute("currentBalance", CurrencyUtil.format(account.getBalance()));

            req.setAttribute("selectedType", type);
            req.setAttribute("startDate", startDate);
            req.setAttribute("endDate", endDate);
            req.setAttribute("searchQuery", search);
            req.setAttribute("selectedSort", sort);

            forward(req, resp, "atm/statement");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error fetching statement: " + e.getMessage());
            forward(req, resp, "atm/statement");
        }
    }
}
