package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.util.CurrencyUtil;
import com.apexbank.util.MaskingUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for the Realistic ATM Kiosk hardware simulator view.
 */
@WebServlet(name = "KioskServlet", urlPatterns = {"/atm/kiosk"})
public class KioskServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);

        ATMCard card = getSessionCard(req);
        Account account = getSessionAccount(req);
        Customer customer = getSessionCustomer(req);

        if (card != null && account != null) {
            req.setAttribute("customerName", customer != null ? customer.getFullName() : "VALUED CARDHOLDER");
            req.setAttribute("maskedCard", card.getMaskedCardNumber());
            req.setAttribute("maskedAccount", MaskingUtil.maskAccountNumber(account.getAccountNumber()));
            req.setAttribute("availableBalance", CurrencyUtil.format(account.getBalance()));
            req.setAttribute("accountType", account.getAccountType());
        }

        forward(req, resp, "atm/kiosk");
    }
}
