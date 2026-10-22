package com.apexbank.controller;

import com.apexbank.exception.BankingException;
import com.apexbank.model.ATMCard;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for updating ATM PIN securely.
 */
@WebServlet(name = "ChangePinServlet", urlPatterns = {"/atm/changepin"})
public class ChangePinServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        forward(req, resp, "atm/changepin");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ATMCard card = getSessionCard(req);
        String oldPin = req.getParameter("oldPin");
        String newPin = req.getParameter("newPin");
        String confirmPin = req.getParameter("confirmPin");

        try {
            if (newPin == null || confirmPin == null || !newPin.trim().equals(confirmPin.trim())) {
                throw new BankingException("New PIN and Confirm PIN do not match.");
            }

            boolean success = atmService.changePin(card.getCardNumber(), oldPin, newPin);
            if (success) {
                req.setAttribute("successMessage", "Your ATM PIN has been updated successfully. Please remember your new PIN.");
            } else {
                req.setAttribute("errorMessage", "Failed to update PIN. Please try again.");
            }
            forward(req, resp, "atm/changepin");
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            forward(req, resp, "atm/changepin");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Unexpected error changing PIN: " + e.getMessage());
            forward(req, resp, "atm/changepin");
        }
    }
}
