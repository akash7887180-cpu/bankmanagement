package com.apexbank.dao;

import com.apexbank.model.Loan;
import com.apexbank.model.LoanCategory;
import com.apexbank.model.LoanStatus;
import com.apexbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {

    private static final String BASE_SELECT =
            "SELECT l.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, a.account_number " +
            "FROM loans l " +
            "JOIN customers c ON l.customer_id = c.customer_id " +
            "JOIN accounts a ON l.account_id = a.account_id ";

    @Override
    public boolean create(Loan loan) {
        String sql = "INSERT INTO loans (loan_reference, account_id, customer_id, loan_category, loan_type, " +
                "amount, tenure_months, interest_rate, monthly_emi, purpose, monthly_income, employment_type, " +
                "gold_weight_grams, gold_karat, gold_market_rate, ornament_details, status, admin_remarks) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, loan.getLoanReference());
            ps.setInt(2, loan.getAccountId());
            ps.setInt(3, loan.getCustomerId());
            ps.setString(4, loan.getLoanCategory().name());
            ps.setString(5, loan.getLoanType());
            ps.setBigDecimal(6, loan.getAmount());
            ps.setInt(7, loan.getTenureMonths());
            ps.setBigDecimal(8, loan.getInterestRate());
            ps.setBigDecimal(9, loan.getMonthlyEmi());
            ps.setString(10, loan.getPurpose());
            ps.setBigDecimal(11, loan.getMonthlyIncome());
            ps.setString(12, loan.getEmploymentType());

            if (loan.getGoldWeightGrams() != null) ps.setBigDecimal(13, loan.getGoldWeightGrams());
            else ps.setNull(13, Types.DECIMAL);

            if (loan.getGoldKarat() != null) ps.setInt(14, loan.getGoldKarat());
            else ps.setNull(14, Types.INTEGER);

            if (loan.getGoldMarketRate() != null) ps.setBigDecimal(15, loan.getGoldMarketRate());
            else ps.setNull(15, Types.DECIMAL);

            ps.setString(16, loan.getOrnamentDetails());
            ps.setString(17, loan.getStatus() != null ? loan.getStatus().name() : LoanStatus.PENDING.name());
            ps.setString(18, loan.getAdminRemarks());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) loan.setLoanId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Loan findById(int loanId) {
        String sql = BASE_SELECT + "WHERE l.loan_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Loan findByReference(String loanReference) {
        String sql = BASE_SELECT + "WHERE l.loan_reference = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loanReference);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Loan> findByCustomerId(int customerId) {
        List<Loan> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE l.customer_id = ? ORDER BY l.applied_date DESC";
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
    public List<Loan> findByAccountId(int accountId) {
        List<Loan> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE l.account_id = ? ORDER BY l.applied_date DESC";
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
    public List<Loan> findAll() {
        List<Loan> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY l.applied_date DESC";
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
    public boolean updateStatus(int loanId, LoanStatus status, String adminRemarks) {
        String sql = "UPDATE loans SET status = ?, admin_remarks = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, adminRemarks);
            ps.setInt(3, loanId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean markDisbursed(int loanId, String adminRemarks) {
        String sql = "UPDATE loans SET status = ?, admin_remarks = ?, disbursed_at = CURRENT_TIMESTAMP WHERE loan_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, LoanStatus.DISBURSED.name());
            ps.setString(2, adminRemarks);
            ps.setInt(3, loanId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countPending() {
        String sql = "SELECT COUNT(*) FROM loans WHERE status = 'PENDING'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setLoanReference(rs.getString("loan_reference"));
        loan.setAccountId(rs.getInt("account_id"));
        loan.setCustomerId(rs.getInt("customer_id"));
        loan.setLoanCategory(LoanCategory.valueOf(rs.getString("loan_category")));
        loan.setLoanType(rs.getString("loan_type"));
        loan.setAmount(rs.getBigDecimal("amount"));
        loan.setTenureMonths(rs.getInt("tenure_months"));
        loan.setInterestRate(rs.getBigDecimal("interest_rate"));
        loan.setMonthlyEmi(rs.getBigDecimal("monthly_emi"));
        loan.setPurpose(rs.getString("purpose"));
        loan.setMonthlyIncome(rs.getBigDecimal("monthly_income"));
        loan.setEmploymentType(rs.getString("employment_type"));

        loan.setGoldWeightGrams(rs.getBigDecimal("gold_weight_grams"));
        int karat = rs.getInt("gold_karat");
        loan.setGoldKarat(rs.wasNull() ? null : karat);
        loan.setGoldMarketRate(rs.getBigDecimal("gold_market_rate"));
        loan.setOrnamentDetails(rs.getString("ornament_details"));

        loan.setStatus(LoanStatus.valueOf(rs.getString("status")));
        loan.setAdminRemarks(rs.getString("admin_remarks"));
        loan.setAppliedDate(rs.getTimestamp("applied_date"));
        loan.setDisbursedAt(rs.getTimestamp("disbursed_at"));

        try {
            loan.setCustomerName(rs.getString("customer_name"));
            loan.setAccountNumber(rs.getString("account_number"));
        } catch (SQLException ignored) {}

        return loan;
    }
}
