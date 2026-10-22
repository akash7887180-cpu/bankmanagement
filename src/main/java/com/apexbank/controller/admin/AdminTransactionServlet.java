package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.model.Transaction;
import com.apexbank.service.AdminService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for viewing system-wide transactions in the admin console.
 */
@WebServlet(name = "AdminTransactionServlet", urlPatterns = {"/admin/transactions"})
public class AdminTransactionServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AdminService adminService = ServiceFactory.getAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Transaction> transactions = adminService.getAllRecentTransactions(100);
            req.setAttribute("transactions", transactions);
            forward(req, resp, "admin/transactions");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error fetching transactions: " + e.getMessage());
            forward(req, resp, "admin/transactions");
        }
    }
}
