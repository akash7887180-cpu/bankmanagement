package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.model.Admin;
import com.apexbank.model.SupportTicket;
import com.apexbank.model.TicketStatus;
import com.apexbank.service.ServiceFactory;
import com.apexbank.service.SupportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@WebServlet(name = "AdminTicketServlet", urlPatterns = {"/admin/tickets"})
public class AdminTicketServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final SupportService supportService = ServiceFactory.getSupportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<SupportTicket> allTickets = supportService.getAllTickets();
        req.setAttribute("tickets", allTickets);

        forward(req, resp, "admin/tickets");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Admin admin = getSessionAdmin(req);
        String adminName = admin != null ? admin.getUsername() : "Manager";

        String ticketIdStr = req.getParameter("ticketId");
        String statusStr = req.getParameter("status");
        String remarks = req.getParameter("resolutionRemarks");

        if (ticketIdStr == null || ticketIdStr.trim().isEmpty()) {
            redirect(resp, req.getContextPath() + "/admin/tickets?error=Invalid+ticket+ID.");
            return;
        }

        try {
            int ticketId = Integer.parseInt(ticketIdStr.trim());
            TicketStatus status = TicketStatus.valueOf(statusStr != null ? statusStr : "RESOLVED");

            boolean ok = supportService.updateTicketStatus(ticketId, status, remarks, adminName);
            if (ok) {
                redirect(resp, req.getContextPath() + "/admin/tickets?message=Ticket+status+updated+to+" + status + "+successfully.");
            } else {
                redirect(resp, req.getContextPath() + "/admin/tickets?error=Failed+to+update+ticket.");
            }
        } catch (Exception e) {
            redirect(resp, req.getContextPath() + "/admin/tickets?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}
