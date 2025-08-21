package org.skypro.recommendation.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KnowledgeBaseService {
    private final JdbcTemplate jdbcTemplate;

    public KnowledgeBaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable("userOfCache")
    public boolean userOf(String productType, UUID userId) {
        String sql = "SELECT COUNT(*) > 0 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, userId.toString(), productType));
    }

    @Cacheable("activeUserOfCache")
    public boolean activeUserOf(String productType, UUID userId) {
        String sql = "SELECT COUNT(*) >= 5 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, userId.toString(), productType));
    }

    @Cacheable("transactionSumCompareCache")
    public boolean transactionSumCompare(String productType, String transactionType,
                                         String operator, int value, UUID userId) {
        String sql = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ? AND t.operation_type = ?";
        Double sum = jdbcTemplate.queryForObject(
                sql, Double.class, userId.toString(), productType, transactionType);

        if (sum == null) sum = 0.0;

        return switch (operator) {
            case ">" -> sum > value;
            case "<" -> sum < value;
            case "=" -> sum == value;
            case ">=" -> sum >= value;
            case "<=" -> sum <= value;
            default -> false;
        };
    }

    @Cacheable("transactionSumCompareDepositWithdrawCache")
    public boolean transactionSumCompareDepositWithdraw(String productType,
                                                        String operator, UUID userId) {
        String sql = "SELECT " +
                "COALESCE(SUM(CASE WHEN t.operation_type = 'DEPOSIT' THEN t.amount END), 0) as deposit, " +
                "COALESCE(SUM(CASE WHEN t.operation_type = 'WITHDRAW' THEN t.amount END), 0) as withdraw " +
                "FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";

        var result = jdbcTemplate.queryForMap(sql, userId.toString(), productType);
        double deposit = ((Number) result.get("deposit")).doubleValue();
        double withdraw = ((Number) result.get("withdraw")).doubleValue();

        return switch (operator) {
            case ">" -> deposit > withdraw;
            case "<" -> deposit < withdraw;
            case "=" -> deposit == withdraw;
            case ">=" -> deposit >= withdraw;
            case "<=" -> deposit <= withdraw;
            default -> false;
        };
    }
}
