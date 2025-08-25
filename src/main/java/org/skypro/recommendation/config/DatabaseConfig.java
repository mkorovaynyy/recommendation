package org.skypro.recommendation.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Конфигурация основной базы данных
 * Настраивает подключение к основной БД транзакций и создает JdbcTemplate
 */
@Configuration
public class DatabaseConfig {

    /**
     * Создание свойств для основной базы данных
     *
     * @return свойства DataSource для основной БД
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Создание источника данных для основной БД
     *
     * @param properties свойства DataSource
     * @return настроенный DataSource
     */
    @Primary
    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Создание JdbcTemplate для работы с основной БД
     *
     * @param dataSource источник данных
     * @return настроенный JdbcTemplate
     */
    @Primary
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}