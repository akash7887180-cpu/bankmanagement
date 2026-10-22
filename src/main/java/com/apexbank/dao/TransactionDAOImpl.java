package com.apexbank.dao;

import com.apexbank.model.Transaction;
import com.apexbank.model.TransactionType;
import com.apexbank.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of TransactionDAO.
 */
public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public int create(Transaction txn, Connection conn) {
        String sql = "INSERT INTO transactions (reference_number, account_id, card_id, transaction_type, amount, balance_after, destination_account_id, description, channel, atm_terminal_id, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, txn.getReferenceNumber());
            ps.setInt(2, txn.getAccountId());
            if (txn.getCardId() != null && txn.getCardId() > 0) {
                ps.setInt(3, txn.getCardId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, txn.getTransactionType().name());
            ps.setDouble(5, txn.getAmount());
            ps.setDouble(6, txn.getBalanceAfter());
            if (txn.getDestinationAccountId() != null && txn.getDestinationAccountId() > 0) {
                ps.setInt(7, txn.getDestinationAccountId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, txn.getDescription());
            ps.setString(9, txn.getChannel() != null ? txn.getChannel() : "ATM");
            ps.setString(10, txn.getAtmTerminalId() != null ? txn.getAtmTerminalId() : "APEX-ATM-042");
            ps.setString(11, txn.getStatus() != null ? txn.getStatus() : "SUCCESS");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        txn.setTransactionId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException ignored) {}
            }
        }
        return -1;
    }

    @Override
    public int create(Transaction txn) {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return create(txn, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Transaction findById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction findByReference(String referenceNumber) {
        String sql = "SELECT * FROM transactions WHERE reference_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, referenceNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> findLastTransactions(int accountId, int limit) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setInt(2, limit > 0 ? limit : 10);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transaction> findAllByAccount(int accountId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transaction> findFiltered(int accountId, String type, String startDate, String endDate, String search) {
        List<Transaction> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM transactions WHERE account_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(accountId);

        if (type != null && !type.trim().isEmpty() && !"ALL".equalsIgnoreCase(type)) {
            sql.append(" AND transaction_type = ?");
            params.add(type.trim());
        }

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND DATE(created_at) >= ?");
            params.add(startDate.trim());
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND DATE(created_at) <= ?");
            params.add(endDate.trim());
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (reference_number LIKE ? OR description LIKE ?)");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }

        sql.append(" ORDER BY created_at DESC");

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transaction> findAllRecent(int limit) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 20);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double getTotalDeposits() {
        String sql = "SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE transaction_type IN ('DEPOSIT', 'TRANSFER_IN') AND status = 'SUCCESS'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public double getTotalWithdrawals() {
        String sql = "SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE transaction_type IN ('WITHDRAWAL', 'FAST_CASH', 'TRANSFER_OUT') AND status = 'SUCCESS'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public int getTodayTransactionCount() {
        String sql = "SELECT COUNT(*) FROM transactions WHERE DATE(created_at) = CURRENT_DATE()";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double getTodayVolume() {
        String sql = "SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE DATE(created_at) = CURRENT_DATE() AND status = 'SUCCESS'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setReferenceNumber(rs.getString("reference_number"));
        t.setAccountId(rs.getInt("account_id"));
        int cId = rs.getInt("card_id");
        t.setCardId(rs.wasNull() ? null : cId);
        t.setTransactionType(TransactionType.fromString(rs.getString("transaction_type")));
        t.setAmount(rs.getDouble("amount"));
        t.setBalanceAfter(rs.getDouble("balance_after"));
        int destId = rs.getInt("destination_account_id");
        t.setDestinationAccountId(rs.wasNull() ? null : destId);
        t.setDescription(rs.getString("description"));
        t.setChannel(rs.getString("channel"));
        t.setAtmTerminalId(rs.getString("atm_terminal_id"));
        t.setStatus(rs.getString("status"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        return t;
    }
}
