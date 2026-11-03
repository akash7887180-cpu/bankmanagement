package com.apexbank.controller;

import com.apexbank.model.*;
import com.apexbank.service.ServiceFactory;
import com.apexbank.service.SupportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "SupportServlet", urlPatterns = {"/atm/support", "/support"})
public class SupportServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final SupportService supportService = ServiceFactory.getSupportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Customer customer = getSessionCustomer(req);
        Account account = getSessionAccount(req);

        if (customer != null && account != null) {
            List<SupportTicket> myTickets = supportService.getCustomerTickets(customer.getCustomerId());
            req.setAttribute("myTickets", myTickets);
        }

        String prefillType = req.getParameter("type");
        req.setAttribute("prefillType", prefillType);

        forward(req, resp, "atm/support");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Customer customer = getSessionCustomer(req);
        Account account = getSessionAccount(req);
        ATMCard card = getSessionCard(req);

        if (customer == null || account == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please+insert+your+ATM+card+and+PIN+to+access+support+tickets.");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("emergencyBlock".equalsIgnoreCase(action)) {
                if (card == null) {
                    resp.sendRedirect(req.getContextPath() + "/atm/support?error=No+active+card+found+in+session.");
                    return;
                }
                String reason = req.getParameter("reason");
                if (reason == null || reason.trim().isEmpty()) {
                    reason = "Customer reported lost/stolen card emergency.";
                }
                boolean locked = supportService.emergencyBlockCard(card.getCardId(), reason);
                if (locked) {
                    card.setStatus(CardStatus.LOCKED);
                    resp.sendRedirect(req.getContextPath() + "/atm/support?success=Your+ATM+card+has+been+<strong>LOCKED+IMMEDIATELY</strong>.+No+unauthorized+withdrawals+can+occur.+An+emergency+security+ticket+has+been+opened.");
                    return;
                }
            } else if ("failedAtmCash".equalsIgnoreCase(action)) {
                String txnRef = req.getParameter("transactionReference");
                String amountStr = req.getParameter("disputedAmount");
                String terminal = req.getParameter("atmTerminalId");

                BigDecimal amount = (amountStr != null && !amountStr.trim().isEmpty()) ?
                        new BigDecimal(amountStr.replaceAll("[^0-9.]", "")) : null;

                Integer cardId = (card != null) ? card.getCardId() : null;
                SupportTicket tck = supportService.reportFailedAtmCash(customer.getCustomerId(),
                        account.getAccountId(), cardId, txnRef, amount, terminal);

                if (tck != null) {
                    resp.sendRedirect(req.getContextPath() + "/atm/support?success=ATM+Cash+Dispute+filed+successfully!+Ticket+Number:+<strong>" + tck.getTicketNumber() + "</strong>.+Investigation+in+progress.");
                    return;
                }
            } else {
                // Standard Grievance / Assistance Ticket
                String typeStr = req.getParameter("issueType");
                String priorityStr = req.getParameter("priority");
                String subject = req.getParameter("subject");
                String description = req.getParameter("description");
                String txnRef = req.getParameter("transactionReference");
                String amountStr = req.getParameter("disputedAmount");

                IssueType issueType = IssueType.valueOf(typeStr != null ? typeStr : "GENERAL_INQUIRY");
                TicketPriority priority = TicketPriority.valueOf(priorityStr != null ? priorityStr : "MEDIUM");
                BigDecimal disputedAmount = (amountStr != null && !amountStr.trim().isEmpty()) ?
                        new BigDecimal(amountStr.replaceAll("[^0-9.]", "")) : null;

                Integer cardId = (card != null) ? card.getCardId() : null;
                SupportTicket tck = supportService.raiseTicket(customer.getCustomerId(), account.getAccountId(),
                        cardId, issueType, priority, subject, description, txnRef, disputedAmount);

                if (tck != null) {
                    resp.sendRedirect(req.getContextPath() + "/atm/support?success=Support+ticket+created+successfully!+Ticket+Number:+<strong>" + tck.getTicketNumber() + "</strong>.+Our+helpdesk+will+resolve+it+promptly.");
                    return;
                }
            }
            resp.sendRedirect(req.getContextPath() + "/atm/support?error=Unable+to+process+request.+Please+try+again.");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/atm/support?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}
