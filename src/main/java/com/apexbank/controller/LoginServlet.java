package com.apexbank.controller;

import com.apexbank.dao.DAOFactory;
import com.apexbank.exception.BankingException;
import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.CardStatus;
import com.apexbank.model.Customer;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for ATM Card insertion and PIN authentication.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/atm/login"})
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String quickCard = req.getParameter("quickCard");
        String quickPin = req.getParameter("quickPin");

        // 1-Click Launch from Admin Panel as any customer
        if (quickCard != null && !quickCard.trim().isEmpty()) {
            try {
                String ip = getClientIp(req);
                String userAgent = req.getHeader("User-Agent");
                ATMCard card = atmService.authenticate(quickCard.trim(), quickPin != null ? quickPin.trim() : "1234", ip, userAgent);
                Account account = atmService.getAccountByCard(card.getCardNumber());
                Customer customer = atmService.getCustomerByCard(card.getCardNumber());

                HttpSession session = req.getSession(true);
                session.setAttribute("SESSION_CARD", card);
                session.setAttribute("SESSION_ACCOUNT", account);
                session.setAttribute("SESSION_CUSTOMER", customer);
                session.setAttribute("SESSION_LOGIN_TIME", new Timestamp(System.currentTimeMillis()));

                redirect(resp, req.getContextPath() + "/atm/dashboard");
                return;
            } catch (Exception e) {
                req.setAttribute("errorMessage", "Quick launch error: " + e.getMessage());
            }
        }

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("SESSION_CARD") != null) {
            redirect(resp, req.getContextPath() + "/atm/dashboard");
            return;
        }

        String error = req.getParameter("error");
        String message = req.getParameter("message");
        if (error != null) req.setAttribute("errorMessage", error);
        if (message != null) req.setAttribute("successMessage", message);

        // Fetch dynamic list of all cards and customers from database
        try {
            List<ATMCard> allCards = DAOFactory.getATMCardDAO().findAll();
            List<Map<String, Object>> cardProfiles = new ArrayList<>();
            for (ATMCard c : allCards) {
                if (c.getStatus() == CardStatus.ACTIVE) {
                    Customer cust = DAOFactory.getCustomerDAO().findById(c.getCustomerId());
                    Account acc = DAOFactory.getAccountDAO().findById(c.getAccountId());
                    if (cust != null && acc != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("card", c);
                        map.put("customer", cust);
                        map.put("account", acc);
                        cardProfiles.add(map);
                    }
                }
            }
            req.setAttribute("cardProfiles", cardProfiles);
        } catch (Exception ignored) {}

        forward(req, resp, "auth/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cardNumber = req.getParameter("cardNumber");
        String pin = req.getParameter("pin");
        String returnToKiosk = req.getParameter("mode");

        try {
            String ip = getClientIp(req);
            String userAgent = req.getHeader("User-Agent");

            ATMCard card = atmService.authenticate(cardNumber, pin, ip, userAgent);
            Account account = atmService.getAccountByCard(card.getCardNumber());
            Customer customer = atmService.getCustomerByCard(card.getCardNumber());

            // Establish secure session
            HttpSession session = req.getSession(true);
            session.setAttribute("SESSION_CARD", card);
            session.setAttribute("SESSION_ACCOUNT", account);
            session.setAttribute("SESSION_CUSTOMER", customer);
            session.setAttribute("SESSION_LOGIN_TIME", new Timestamp(System.currentTimeMillis()));

            if ("kiosk".equalsIgnoreCase(returnToKiosk)) {
                redirect(resp, req.getContextPath() + "/atm/kiosk");
            } else {
                redirect(resp, req.getContextPath() + "/atm/dashboard");
            }
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("prevCard", cardNumber);
            forward(req, resp, "auth/login");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "An unexpected error occurred during login. Please try again.");
            forward(req, resp, "auth/login");
        }
    }
}
