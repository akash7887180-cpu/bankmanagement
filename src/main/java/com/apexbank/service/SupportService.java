package com.apexbank.service;

import com.apexbank.model.IssueType;
import com.apexbank.model.SupportTicket;
import com.apexbank.model.TicketPriority;
import com.apexbank.model.TicketStatus;

import java.math.BigDecimal;
import java.util.List;

public interface SupportService {
    SupportTicket raiseTicket(int customerId, int accountId, Integer cardId,
                              IssueType issueType, TicketPriority priority,
                              String subject, String description,
                              String transactionReference, BigDecimal disputedAmount);

    SupportTicket reportFailedAtmCash(int customerId, int accountId, Integer cardId,
                                      String txnRef, BigDecimal amount, String atmTerminal);

    boolean emergencyBlockCard(int cardId, String reason);

    List<SupportTicket> getCustomerTickets(int customerId);
    List<SupportTicket> getAccountTickets(int accountId);
    List<SupportTicket> getAllTickets();
    SupportTicket getTicketById(int ticketId);
    SupportTicket getTicketByNumber(String ticketNumber);

    boolean updateTicketStatus(int ticketId, TicketStatus status, String resolutionRemarks, String resolvedBy);
}
