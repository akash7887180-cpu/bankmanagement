package com.apexbank.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Invalidate sessions and safely log out users.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout", "/atm/logout", "/admin/logout"})
public class LogoutServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        boolean isAdmin = uri.contains("/admin");

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (isAdmin) {
            redirect(resp, req.getContextPath() + "/admin/login?message=You+have+been+logged+out+securely.");
        } else {
            redirect(resp, req.getContextPath() + "/login?message=Thank+you+for+using+Apex+ATM.+Card+safely+ejected.");
        }
    }
}
