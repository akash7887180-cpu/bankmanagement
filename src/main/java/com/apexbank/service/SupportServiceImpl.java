package com.apexbank.service;

import com.apexbank.dao.ATMCardDAO;
import com.apexbank.dao.AuditDAO;
import com.apexbank.dao.DAOFactory;
import com.apexbank.dao.SupportTicketDAO;
import com.apexbank.model.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SupportServiceImpl implements SupportService {

    private final SupportTicketDAO ticketDAO = DAOFactory.getSupportTicketDAO();
    private final ATMCardDAO cardDAO = DAOFactory.getATMCardDAO();
    private final AuditDAO auditDAO = DAOFactory.getAuditDAO();

    @Override
    public SupportTicket raiseTicket(int customerId, int accountId, Integer cardId,
                                     IssueType issueType, TicketPriority priority,
                                     String subject, String description,
                                     String transactionReference, BigDecimal disputedAmount) {

        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket subject cannot be empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Detailed description is required for problem solving.");
        }

        String tckNumber = "TCK-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" +
                UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        SupportTicket ticket = new SupportTicket();
        ticket.setTicketNumber(tckNumber);
        ticket.setCustomerId(customerId);
        ticket.setAccountId(accountId);
        ticket.setCardId(cardId);
        ticket.setIssueType(issueType != null ? issueType : IssueType.GENERAL_INQUIRY);
        ticket.setPriority(priority != null ? priority : TicketPriority.MEDIUM);
        ticket.setSubject(subject.trim());
        ticket.setDescription(description.trim());
        ticket.setTransactionReference(transactionReference != null ? transactionReference.trim() : null);
        ticket.setDisputedAmount(disputedAmount);
        ticket.setStatus(TicketStatus.OPEN);

        boolean created = ticketDAO.create(ticket);
        if (created) {
            auditDAO.log(new AuditLog("TICKET_RAISED", "CUSTOMER",
                    "New support ticket created: " + tckNumber + " [" + issueType + "] " + subject));
            return ticket;
        }
        return null;
    }

    @Override
    public SupportTicket reportFailedAtmCash(int customerId, int accountId, Integer cardId,
                                             String txnRef, BigDecimal amount, String atmTerminal) {
        String sub = "ATM Cash Not Dispensed: Ref " + (txnRef != null ? txnRef : "Recent TXN");
        String desc = "Customer reported that ATM terminal " + (atmTerminal != null ? atmTerminal : "APEX-ATM-042") +
                " debited " + (amount != null ? amount : "the requested amount") +
                " but cash was not dispensed. Immediate reversal and network reconciliation requested.";

        return raiseTicket(customerId, accountId, cardId,
                IssueType.FAILED_ATM_CASH, TicketPriority.HIGH,
                sub, desc, txnRef, amount);
    }

    @Override
    public boolean emergencyBlockCard(int cardId, String reason) {
        ATMCard card = cardDAO.findById(cardId);
        if (card == null) return false;

        boolean updated = cardDAO.updateStatus(cardId, CardStatus.LOCKED);
        if (updated) {
            auditDAO.log(new AuditLog("EMERGENCY_CARD_LOCK", "CUSTOMER",
                    "Card " + card.getCardNumber() + " LOCKED immediately by emergency request. Reason: " + reason));

            // Automatically generate an emergency security ticket
            raiseTicket(card.getCustomerId(), card.getAccountId(), cardId,
                    IssueType.LOST_CARD_FRAUD, TicketPriority.EMERGENCY,
                    "Emergency Card Block Triggered: " + card.getMaskedCardNumber(),
                    "Card was locked due to customer emergency request. Reason: " + reason +
                            ". Card status is currently LOCKED to protect customer balance.",
                    null, null);
        }
        return updated;
    }

    @Override
    public List<SupportTicket> getCustomerTickets(int customerId) {
        return ticketDAO.findByCustomerId(customerId);
    }

    @Override
    public List<SupportTicket> getAccountTickets(int accountId) {
        return ticketDAO.findByAccountId(accountId);
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        return ticketDAO.findAll();
    }

    @Override
    public SupportTicket getTicketById(int ticketId) {
        return ticketDAO.findById(ticketId);
    }

    @Override
    public SupportTicket getTicketByNumber(String ticketNumber) {
        return ticketDAO.findByTicketNumber(ticketNumber);
    }

    @Override
    public boolean updateTicketStatus(int ticketId, TicketStatus status, String resolutionRemarks, String resolvedBy) {
        boolean updated = ticketDAO.updateStatus(ticketId, status, resolutionRemarks, resolvedBy);
        if (updated) {
            auditDAO.log(new AuditLog("TICKET_STATUS_UPDATE", resolvedBy != null ? resolvedBy : "ADMIN",
                    "Ticket ID " + ticketId + " updated to " + status + ". Remarks: " + resolutionRemarks));
        }
        return updated;
    }
}
