package org.skypro.recommendation.repository.rule;

import org.skypro.recommendation.model.rule.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с динамическими правилами
 * Предоставляет методы для CRUD операций с правилами рекомендаций
 */
@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {

    /**
     * Удаление правила по идентификатору продукта
     *
     * @param productId идентификатор продукта
     */
    void deleteByProductId(UUID productId);
}
