package org.skypro.recommendation.service.rule;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SavingRule {
    private final JdbcTemplate jdbcTemplate;

    public SavingRule(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean evaluateWithMinAmount(UUID userId, double minAmount) {
        String sql = "SELECT COALESCE(SUM(t.amount), 0) >= ? "
                + "FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? "
                + "AND p.type = 'SAVING' "
                + "AND t.operation_type = 'DEPOSIT'";
        Double total = jdbcTemplate.queryForObject(
                sql, Double.class, minAmount, userId.toString());
        return total != null && total >= minAmount;
    }
}