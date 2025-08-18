package org.skypro.recommendation.repository;

import org.skypro.recommendation.model.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT id, name, description FROM products";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToProduct(rs));
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}