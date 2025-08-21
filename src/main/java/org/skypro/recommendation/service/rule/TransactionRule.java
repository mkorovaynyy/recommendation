package org.skypro.recommendation.service.rule;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionRule {
    private final JdbcTemplate jdbcTemplate;

    public TransactionRule(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean minTotalDeposit(UUID userId, double minAmount) {
        String sql = "SELECT (COALESCE(SUM(CASE WHEN p.type = 'DEBIT' THEN t.amount END), 0) >= ? "
                + "OR COALESCE(SUM(CASE WHEN p.type = 'SAVING' THEN t.amount END), 0) >= ? "
                + "FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND t.operation_type = 'DEPOSIT'";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, minAmount, minAmount, userId.toString()));
    }

    public boolean debitDepositExceedsWithdrawal(UUID userId) {
        String sql = "SELECT "
                + "COALESCE(SUM(CASE WHEN t.operation_type = 'DEPOSIT' THEN t.amount END), 0) > "
                + "COALESCE(SUM(CASE WHEN t.operation_type = 'WITHDRAWAL' THEN t.amount END), 0) "
                + "FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'DEBIT'";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, userId.toString()));
    }

    public boolean minTotalWithdrawal(UUID userId, double minAmount) {
        String sql = "SELECT COALESCE(SUM(t.amount), 0) >= ? "
                + "FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? "
                + "AND p.type = 'DEBIT' "
                + "AND t.operation_type = 'WITHDRAWAL'";
        Double total = jdbcTemplate.queryForObject(
                sql, Double.class, minAmount, userId.toString());
        return total != null && total >= minAmount;
    }
}