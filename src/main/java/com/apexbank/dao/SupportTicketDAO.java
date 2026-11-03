package com.apexbank.dao;

import com.apexbank.model.SupportTicket;
import com.apexbank.model.TicketStatus;
import java.util.List;

public interface SupportTicketDAO {
    boolean create(SupportTicket ticket);
    SupportTicket findById(int ticketId);
    SupportTicket findByTicketNumber(String ticketNumber);
    List<SupportTicket> findByCustomerId(int customerId);
    List<SupportTicket> findByAccountId(int accountId);
    List<SupportTicket> findAll();
    boolean updateStatus(int ticketId, TicketStatus status, String resolutionRemarks, String resolvedBy);
    int countOpen();
}
