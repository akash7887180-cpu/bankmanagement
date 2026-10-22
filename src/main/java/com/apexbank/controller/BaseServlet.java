package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.Admin;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Base Abstract Controller for all Servlets.
 * Demonstrates OOP Inheritance and Controller Layer Abstraction.
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected static final String VIEW_PREFIX = "/WEB-INF/views/";
    protected static final String VIEW_SUFFIX = ".jsp";
    protected final Gson gson = new Gson();

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String viewName)
            throws ServletException, IOException {
        String path = VIEW_PREFIX + viewName + VIEW_SUFFIX;
        req.getRequestDispatcher(path).forward(req, resp);
    }

    protected void redirect(HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(path);
    }

    protected void sendJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }

    protected ATMCard getSessionCard(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (ATMCard) session.getAttribute("SESSION_CARD") : null;
    }

    protected Account getSessionAccount(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (Account) session.getAttribute("SESSION_ACCOUNT") : null;
    }

    protected Customer getSessionCustomer(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (Customer) session.getAttribute("SESSION_CUSTOMER") : null;
    }

    protected Admin getSessionAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (Admin) session.getAttribute("SESSION_ADMIN") : null;
    }

    /**
     * Refreshes customer account balance and card state in session from database.
     */
    protected void refreshSessionData(HttpServletRequest req) {
        try {
            ATMCard card = getSessionCard(req);
            if (card != null) {
                ATMService service = ServiceFactory.getATMService();
                Account updatedAccount = service.getAccountByCard(card.getCardNumber());
                Customer updatedCustomer = service.getCustomerByCard(card.getCardNumber());
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.setAttribute("SESSION_ACCOUNT", updatedAccount);
                    session.setAttribute("SESSION_CUSTOMER", updatedCustomer);
                }
            }
        } catch (Exception e) {
            System.err.println("Could not refresh session state: " + e.getMessage());
        }
    }

    protected String getClientIp(HttpServletRequest req) {
        String xf = req.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isEmpty()) {
            return xf.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }
}
