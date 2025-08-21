package org.skypro.recommendation.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID findUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ? OR CONCAT(first_name, ' ', last_name) = ?";
        try {
            return jdbcTemplate.queryForObject(sql, UUID.class, username, username);
        } catch (Exception e) {
            return null;
        }
    }
}