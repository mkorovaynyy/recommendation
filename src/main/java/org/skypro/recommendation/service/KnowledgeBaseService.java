package org.skypro.recommendation.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис для работы с базой знаний
 * Предоставляет методы для проверки условий правил рекомендаций с кэшированием
 */
@Service
public class KnowledgeBaseService {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор сервиса базы знаний
     *
     * @param jdbcTemplate JdbcTemplate для работы с БД
     */
    public KnowledgeBaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверка, является ли пользователь пользователем продукта
     *
     * @param productType тип продукта
     * @param userId      идентификатор пользователя
     * @return true если пользователь является пользователем продукта
     */
    @Cacheable("userOfCache")
    public boolean userOf(String productType, UUID userId) {
        String sql = "SELECT COUNT(*) > 0 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, userId.toString(), productType));
    }

    /**
     * Проверка, является ли пользователь активным пользователем продукта
     *
     * @param productType тип продукта
     * @param userId      идентификатор пользователя
     * @return true если пользователь является активным пользователем продукта
     */
    @Cacheable("activeUserOfCache")
    public boolean activeUserOf(String productType, UUID userId) {
        String sql = "SELECT COUNT(*) >= 5 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, userId.toString(), productType));
    }

    /**
     * Сравнение суммы транзакций по определенным критериям
     *
     * @param productType     тип продукта
     * @param transactionType тип транзакции
     * @param operator        оператор сравнения
     * @param value           значение для сравнения
     * @param userId          идентификатор пользователя
     * @return true если условие выполняется
     */
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

    /**
     * Сравнение сумм депозитов и выводов по продукту
     *
     * @param productType тип продукта
     * @param operator    оператор сравнения
     * @param userId      идентификатор пользователя
     * @return true если условие выполняется
     */
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