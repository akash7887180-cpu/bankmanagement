package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.exception.BankingException;
import com.apexbank.model.Admin;
import com.apexbank.service.AdminService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for Bank Administrator login.
 */
@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/login"})
public class AdminLoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AdminService adminService = ServiceFactory.getAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("SESSION_ADMIN") != null) {
            redirect(resp, req.getContextPath() + "/admin/dashboard");
            return;
        }

        String error = req.getParameter("error");
        String message = req.getParameter("message");
        if (error != null) req.setAttribute("errorMessage", error);
        if (message != null) req.setAttribute("successMessage", message);

        forward(req, resp, "admin/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            Admin admin = adminService.login(username, password, getClientIp(req));
            HttpSession session = req.getSession(true);
            session.setAttribute("SESSION_ADMIN", admin);
            redirect(resp, req.getContextPath() + "/admin/dashboard");
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("prevUsername", username);
            forward(req, resp, "admin/login");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Internal authentication error: " + e.getMessage());
            forward(req, resp, "admin/login");
        }
    }
}
