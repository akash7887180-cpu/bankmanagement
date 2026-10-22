package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.service.AdminService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.CurrencyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Controller for Financial Aggregation and Management Reports.
 */
@WebServlet(name = "AdminReportServlet", urlPatterns = {"/admin/reports"})
public class AdminReportServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AdminService adminService = ServiceFactory.getAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Map<String, Object> stats = adminService.getDashboardStatistics();

            req.setAttribute("stats", stats);
            req.setAttribute("formattedDeposits", CurrencyUtil.format((Double) stats.get("totalDeposits")));
            req.setAttribute("formattedWithdrawals", CurrencyUtil.format((Double) stats.get("totalWithdrawals")));
            req.setAttribute("formattedTotalBalance", CurrencyUtil.format((Double) stats.get("bankTotalBalance")));
            req.setAttribute("formattedTodayVolume", CurrencyUtil.format((Double) stats.get("todayVolume")));

            req.setAttribute("generatedDate", new SimpleDateFormat("dd MMMM yyyy, hh:mm a").format(new Date()));
            req.setAttribute("reportPeriod", new SimpleDateFormat("MMMM yyyy").format(new Date()));

            forward(req, resp, "admin/reports");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error compiling report: " + e.getMessage());
            forward(req, resp, "admin/reports");
        }
    }
}
