package org.skypro.recommendation.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Конфигурация базы данных для правил рекомендаций
 * Настраивает подключение к отдельной БД для хранения динамических правил
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.skypro.recommendation.repository.rule",
        entityManagerFactoryRef = "ruleEntityManagerFactory",
        transactionManagerRef = "ruleTransactionManager"
)
public class RuleDatabaseConfig {

    /**
     * Создание свойств для базы данных правил
     *
     * @return свойства DataSource для БД правил
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSourceProperties ruleDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Создание источника данных для БД правил
     *
     * @param ruleDataSourceProperties свойства DataSource
     * @return настроенный DataSource
     */
    @Bean
    public DataSource ruleDataSource(DataSourceProperties ruleDataSourceProperties) {
        return ruleDataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Создание фабрики EntityManager для БД правил
     *
     * @param builder        билдер для создания EntityManagerFactory
     * @param ruleDataSource источник данных для БД правил
     * @return настроенная фабрика EntityManager
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean ruleEntityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource ruleDataSource) {
        return builder
                .dataSource(ruleDataSource)
                .packages("org.skypro.recommendation.model.rule")
                .build();
    }

    /**
     * Создание менеджера транзакций для БД правил
     *
     * @param ruleEntityManagerFactory фабрика EntityManager
     * @return менеджер транзакций
     */
    @Bean
    public PlatformTransactionManager ruleTransactionManager(
            LocalContainerEntityManagerFactoryBean ruleEntityManagerFactory) {
        return new JpaTransactionManager(
                Objects.requireNonNull(ruleEntityManagerFactory.getObject()));
    }
}