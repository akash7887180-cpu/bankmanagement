package com.apexbank.controller;

import com.apexbank.exception.BankingException;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Receipt;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.CurrencyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for 1-click Fast Cash operations.
 */
@WebServlet(name = "FastCashServlet", urlPatterns = {"/atm/fastcash"})
public class FastCashServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        refreshSessionData(req);
        ATMCard card = getSessionCard(req);
        try {
            double remainingLimit = atmService.getRemainingDailyLimit(card.getCardNumber());
            req.setAttribute("dailyLimitRemaining", CurrencyUtil.format(remainingLimit));
        } catch (Exception ignored) {}
        forward(req, resp, "atm/fastcash");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ATMCard card = getSessionCard(req);
        String amountStr = req.getParameter("amount");

        try {
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new BankingException("Please select a Fast Cash denomination.");
            }
            double amount = Double.parseDouble(amountStr.trim());

            Receipt receipt = atmService.fastCash(card.getCardNumber(), amount);
            refreshSessionData(req);

            req.getSession().setAttribute("LAST_RECEIPT", receipt);
            redirect(resp, req.getContextPath() + "/atm/receipt");
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error dispensing Fast Cash: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
