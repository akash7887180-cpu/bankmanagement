package com.apexbank.dao;

import com.apexbank.model.IssueType;
import com.apexbank.model.SupportTicket;
import com.apexbank.model.TicketPriority;
import com.apexbank.model.TicketStatus;
import com.apexbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupportTicketDAOImpl implements SupportTicketDAO {

    private static final String BASE_SELECT =
            "SELECT t.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, c.phone AS customer_phone, " +
            "a.account_number, cd.card_number " +
            "FROM support_tickets t " +
            "JOIN customers c ON t.customer_id = c.customer_id " +
            "JOIN accounts a ON t.account_id = a.account_id " +
            "LEFT JOIN atm_cards cd ON t.card_id = cd.card_id ";

    @Override
    public boolean create(SupportTicket ticket) {
        String sql = "INSERT INTO support_tickets (ticket_number, customer_id, account_id, card_id, " +
                "issue_type, priority, subject, description, transaction_reference, disputed_amount, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ticket.getTicketNumber());
            ps.setInt(2, ticket.getCustomerId());
            ps.setInt(3, ticket.getAccountId());

            if (ticket.getCardId() != null) ps.setInt(4, ticket.getCardId());
            else ps.setNull(4, Types.INTEGER);

            ps.setString(5, ticket.getIssueType().name());
            ps.setString(6, ticket.getPriority().name());
            ps.setString(7, ticket.getSubject());
            ps.setString(8, ticket.getDescription());
            ps.setString(9, ticket.getTransactionReference());

            if (ticket.getDisputedAmount() != null) ps.setBigDecimal(10, ticket.getDisputedAmount());
            else ps.setNull(10, Types.DECIMAL);

            ps.setString(11, ticket.getStatus() != null ? ticket.getStatus().name() : TicketStatus.OPEN.name());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) ticket.setTicketId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public SupportTicket findById(int ticketId) {
        String sql = BASE_SELECT + "WHERE t.ticket_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SupportTicket findByTicketNumber(String ticketNumber) {
        String sql = BASE_SELECT + "WHERE t.ticket_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SupportTicket> findByCustomerId(int customerId) {
        List<SupportTicket> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE t.customer_id = ? ORDER BY t.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<SupportTicket> findByAccountId(int accountId) {
        List<SupportTicket> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE t.account_id = ? ORDER BY t.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<SupportTicket> findAll() {
        List<SupportTicket> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY t.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateStatus(int ticketId, TicketStatus status, String resolutionRemarks, String resolvedBy) {
        String sql = "UPDATE support_tickets SET status = ?, resolution_remarks = ?, resolved_by = ?, " +
                "resolved_at = CASE WHEN ? = 'RESOLVED' OR ? = 'CLOSED' THEN CURRENT_TIMESTAMP ELSE resolved_at END " +
                "WHERE ticket_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, resolutionRemarks);
            ps.setString(3, resolvedBy);
            ps.setString(4, status.name());
            ps.setString(5, status.name());
            ps.setInt(6, ticketId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countOpen() {
        String sql = "SELECT COUNT(*) FROM support_tickets WHERE status IN ('OPEN', 'IN_PROGRESS')";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private SupportTicket mapRow(ResultSet rs) throws SQLException {
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketId(rs.getInt("ticket_id"));
        ticket.setTicketNumber(rs.getString("ticket_number"));
        ticket.setCustomerId(rs.getInt("customer_id"));
        ticket.setAccountId(rs.getInt("account_id"));

        int cardId = rs.getInt("card_id");
        ticket.setCardId(rs.wasNull() ? null : cardId);

        ticket.setIssueType(IssueType.valueOf(rs.getString("issue_type")));
        ticket.setPriority(TicketPriority.valueOf(rs.getString("priority")));
        ticket.setSubject(rs.getString("subject"));
        ticket.setDescription(rs.getString("description"));
        ticket.setTransactionReference(rs.getString("transaction_reference"));
        ticket.setDisputedAmount(rs.getBigDecimal("disputed_amount"));
        ticket.setStatus(TicketStatus.valueOf(rs.getString("status")));
        ticket.setResolutionRemarks(rs.getString("resolution_remarks"));
        ticket.setResolvedBy(rs.getString("resolved_by"));
        ticket.setResolvedAt(rs.getTimestamp("resolved_at"));
        ticket.setCreatedAt(rs.getTimestamp("created_at"));

        try {
            ticket.setCustomerName(rs.getString("customer_name"));
            ticket.setCustomerPhone(rs.getString("customer_phone"));
            ticket.setAccountNumber(rs.getString("account_number"));
            ticket.setCardNumber(rs.getString("card_number"));
        } catch (SQLException ignored) {}

        return ticket;
    }
}
