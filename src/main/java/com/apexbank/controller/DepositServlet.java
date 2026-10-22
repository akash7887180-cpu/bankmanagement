package com.apexbank.controller;

import com.apexbank.exception.BankingException;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Receipt;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for Cash Deposit operation.
 */
@WebServlet(name = "DepositServlet", urlPatterns = {"/atm/deposit"})
public class DepositServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);
        forward(req, resp, "atm/deposit");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ATMCard card = getSessionCard(req);
        String amountStr = req.getParameter("amount");

        try {
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new BankingException("Please enter a valid deposit amount.");
            }
            double amount = Double.parseDouble(amountStr.trim());

            Receipt receipt = atmService.deposit(card.getCardNumber(), amount);
            refreshSessionData(req);

            // Store receipt in session for display and download
            req.getSession().setAttribute("LAST_RECEIPT", receipt);
            redirect(resp, req.getContextPath() + "/atm/receipt");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid numeric format entered.");
            forward(req, resp, "atm/deposit");
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            forward(req, resp, "atm/deposit");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Unexpected error processing deposit: " + e.getMessage());
            forward(req, resp, "atm/deposit");
        }
    }
}
