package org.skypro.recommendation.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис для работы с пользователями
 * Предоставляет методы для поиска пользователей в базе данных
 */
@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор сервиса пользователей
     *
     * @param jdbcTemplate JdbcTemplate для работы с базой данных
     */
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Поиск ID пользователя по имени пользователя
     *
     * @param username имя пользователя
     * @return ID пользователя или null, если пользователь не найден
     */
    public UUID findUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ? OR CONCAT(first_name, ' ', last_name) = ?";
        try {
            return jdbcTemplate.queryForObject(sql, UUID.class, username, username);
        } catch (Exception e) {
            return null;
        }
    }
}