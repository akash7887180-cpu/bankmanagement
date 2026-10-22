package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.model.Admin;
import com.apexbank.model.AuditLog;
import com.apexbank.model.LoginHistory;
import com.apexbank.model.Transaction;
import com.apexbank.service.AdminService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.CurrencyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller for Bank Administrator main dashboard.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AdminService adminService = ServiceFactory.getAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Admin admin = getSessionAdmin(req);
        if (admin == null) {
            redirect(resp, req.getContextPath() + "/admin/login");
            return;
        }

        try {
            Map<String, Object> stats = adminService.getDashboardStatistics();
            List<Transaction> recentTxns = adminService.getAllRecentTransactions(10);
            List<AuditLog> recentLogs = adminService.getRecentAuditLogs(8);
            List<LoginHistory> recentLogins = adminService.getRecentLoginHistory(8);

            req.setAttribute("admin", admin);
            req.setAttribute("stats", stats);
            req.setAttribute("formattedDeposits", CurrencyUtil.format((Double) stats.get("totalDeposits")));
            req.setAttribute("formattedWithdrawals", CurrencyUtil.format((Double) stats.get("totalWithdrawals")));
            req.setAttribute("formattedTotalBalance", CurrencyUtil.format((Double) stats.get("bankTotalBalance")));
            req.setAttribute("formattedTodayVolume", CurrencyUtil.format((Double) stats.get("todayVolume")));

            req.setAttribute("recentTransactions", recentTxns);
            req.setAttribute("recentLogs", recentLogs);
            req.setAttribute("recentLogins", recentLogins);

            forward(req, resp, "admin/dashboard");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading admin dashboard: " + e.getMessage());
            forward(req, resp, "admin/dashboard");
        }
    }
}
