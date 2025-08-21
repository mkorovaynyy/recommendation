package org.skypro.recommendation.service.rule;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreditRule implements RecommendationRule {
    private final JdbcTemplate jdbcTemplate;

    public CreditRule(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean evaluate(UUID userId) {
        String sql = "SELECT COUNT(*) > 0 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'CREDIT'";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, userId.toString()));
    }
}
