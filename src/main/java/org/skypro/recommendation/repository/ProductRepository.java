package org.skypro.recommendation.repository;

import org.skypro.recommendation.model.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с продуктами
 * Предоставляет методы для получения информации о продуктах из базы данных
 */
@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор репозитория продуктов
     *
     * @param jdbcTemplate JdbcTemplate для работы с БД
     */
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение всех продуктов из базы данных
     *
     * @return список всех продуктов
     */
    public List<Product> getAllProducts() {
        String sql = "SELECT id, name, description FROM products";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToProduct(rs));
    }

    /**
     * Преобразование строки ResultSet в объект Product
     *
     * @param rs ResultSet с данными о продукте
     * @return объект Product
     * @throws SQLException при ошибках работы с БД
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}