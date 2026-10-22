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
 * Controller for Inter-Account Fund Transfers.
 */
@WebServlet(name = "TransferServlet", urlPatterns = {"/atm/transfer"})
public class TransferServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);
        forward(req, resp, "atm/transfer");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ATMCard card = getSessionCard(req);
        String targetAccount = req.getParameter("targetAccount");
        String confirmAccount = req.getParameter("confirmAccount");
        String amountStr = req.getParameter("amount");
        String description = req.getParameter("description");

        try {
            if (targetAccount == null || confirmAccount == null || !targetAccount.trim().equals(confirmAccount.trim())) {
                throw new BankingException("Target account numbers do not match.");
            }
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new BankingException("Transfer amount is required.");
            }
            double amount = Double.parseDouble(amountStr.trim());

            Receipt receipt = atmService.transfer(card.getCardNumber(), targetAccount.trim(), amount, description);
            refreshSessionData(req);

            req.getSession().setAttribute("LAST_RECEIPT", receipt);
            redirect(resp, req.getContextPath() + "/atm/receipt");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid numeric transfer amount.");
            forward(req, resp, "atm/transfer");
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("prevTarget", targetAccount);
            req.setAttribute("prevAmount", amountStr);
            forward(req, resp, "atm/transfer");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Internal error while executing transfer: " + e.getMessage());
            forward(req, resp, "atm/transfer");
        }
    }
}
