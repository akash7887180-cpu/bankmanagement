package com.apexbank.dao;

import com.apexbank.model.ATMCard;
import com.apexbank.model.CardStatus;
import com.apexbank.model.CardType;
import com.apexbank.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of ATMCardDAO.
 */
public class ATMCardDAOImpl implements ATMCardDAO {

    @Override
    public ATMCard findById(int cardId) {
        String sql = "SELECT * FROM atm_cards WHERE card_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
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
    public ATMCard findByCardNumber(String cardNumber) {
        if (cardNumber == null) return null;
        String cleanNumber = cardNumber.replaceAll("[\\s-]+", "").trim();
        String sql = "SELECT * FROM atm_cards WHERE card_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cleanNumber);
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
    public List<ATMCard> findByAccountId(int accountId) {
        List<ATMCard> list = new ArrayList<>();
        String sql = "SELECT * FROM atm_cards WHERE account_id = ?";
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
    public List<ATMCard> findByCustomerId(int customerId) {
        List<ATMCard> list = new ArrayList<>();
        String sql = "SELECT * FROM atm_cards WHERE customer_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
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
    public List<ATMCard> findAll() {
        List<ATMCard> list = new ArrayList<>();
        String sql = "SELECT * FROM atm_cards ORDER BY card_id DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int create(ATMCard card) {
        String sql = "INSERT INTO atm_cards (card_number, account_id, customer_id, card_type, pin_hash, pin_salt, expiry_date, cvv_hash, daily_limit, failed_pin_attempts, status, pin_plain) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, card.getCardNumber().replaceAll("[\\s-]+", ""));
            ps.setInt(2, card.getAccountId());
            ps.setInt(3, card.getCustomerId());
            ps.setString(4, card.getCardType() != null ? card.getCardType().name() : CardType.DEBIT_RUPAY.name());
            ps.setString(5, card.getPinHash());
            ps.setString(6, card.getPinSalt() != null ? card.getPinSalt() : "");
            ps.setDate(7, card.getExpiryDate());
            ps.setString(8, card.getCvvHash() != null ? card.getCvvHash() : "");
            ps.setDouble(9, card.getDailyLimit());
            ps.setInt(10, card.getFailedPinAttempts());
            ps.setString(11, card.getStatus() != null ? card.getStatus().name() : CardStatus.ACTIVE.name());
            ps.setString(12, card.getPinPlain() != null ? card.getPinPlain() : "1234");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        card.setCardId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean updatePin(int cardId, String newPinHash, String newPinSalt) {
        return updatePin(cardId, newPinHash, newPinSalt, null);
    }

    @Override
    public boolean updatePin(int cardId, String newPinHash, String newPinSalt, String newPlainPin) {
        String sql = (newPlainPin != null && !newPlainPin.isEmpty())
                ? "UPDATE atm_cards SET pin_hash = ?, pin_salt = ?, pin_plain = ?, failed_pin_attempts = 0, status = 'ACTIVE' WHERE card_id = ?"
                : "UPDATE atm_cards SET pin_hash = ?, pin_salt = ?, failed_pin_attempts = 0, status = 'ACTIVE' WHERE card_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPinHash);
            ps.setString(2, newPinSalt);
            if (newPlainPin != null && !newPlainPin.isEmpty()) {
                ps.setString(3, newPlainPin);
                ps.setInt(4, cardId);
            } else {
                ps.setInt(3, cardId);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int incrementFailedAttempts(int cardId) {
        String sql = "UPDATE atm_cards SET failed_pin_attempts = failed_pin_attempts + 1, "
                   + "status = CASE WHEN failed_pin_attempts + 1 >= 3 THEN 'LOCKED' ELSE status END "
                   + "WHERE card_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();

            ATMCard updated = findById(cardId);
            return updated != null ? updated.getFailedPinAttempts() : 3;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3;
    }

    @Override
    public boolean resetFailedAttempts(int cardId) {
        String sql = "UPDATE atm_cards SET failed_pin_attempts = 0 WHERE card_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int cardId, CardStatus status) {
        String sql = "UPDATE atm_cards SET status = ?, failed_pin_attempts = (CASE WHEN ? = 'ACTIVE' THEN 0 ELSE failed_pin_attempts END) WHERE card_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, status.name());
            ps.setInt(3, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double getTodayWithdrawalTotal(int cardId) {
        String sql = "SELECT COALESCE(SUM(amount), 0.0) FROM transactions "
                   + "WHERE card_id = ? "
                   + "AND transaction_type IN ('WITHDRAWAL', 'FAST_CASH') "
                   + "AND status = 'SUCCESS' "
                   + "AND DATE(created_at) = CURRENT_DATE()";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public int countTotalCards() {
        String sql = "SELECT COUNT(*) FROM atm_cards";
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

    private ATMCard mapRow(ResultSet rs) throws SQLException {
        ATMCard card = new ATMCard();
        card.setCardId(rs.getInt("card_id"));
        card.setCardNumber(rs.getString("card_number"));
        card.setAccountId(rs.getInt("account_id"));
        card.setCustomerId(rs.getInt("customer_id"));
        card.setCardType(CardType.fromString(rs.getString("card_type")));
        card.setPinHash(rs.getString("pin_hash"));
        card.setPinSalt(rs.getString("pin_salt"));
        card.setExpiryDate(rs.getDate("expiry_date"));
        card.setCvvHash(rs.getString("cvv_hash"));
        card.setDailyLimit(rs.getDouble("daily_limit"));
        card.setFailedPinAttempts(rs.getInt("failed_pin_attempts"));
        card.setStatus(CardStatus.fromString(rs.getString("status")));
        card.setIssuedAt(rs.getTimestamp("issued_at"));
        try {
            card.setPinPlain(rs.getString("pin_plain"));
        } catch (Exception ignored) {
            card.setPinPlain("1234");
        }
        return card;
    }
}
