package org.skypro.recommendation.service;

import org.skypro.recommendation.model.rule.DynamicRule;
import org.skypro.recommendation.repository.rule.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с правилами рекомендаций
 * Содержит бизнес-логику для операций с правилами
 */
@Service
public class RuleService {
    private final DynamicRuleRepository dynamicRuleRepository;

    /**
     * Конструктор сервиса правил
     *
     * @param dynamicRuleRepository репозиторий динамических правил
     */
    public RuleService(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    /**
     * Создание нового правила
     *
     * @param dynamicRule правило для создания
     * @return созданное правило
     */
    public DynamicRule createRule(DynamicRule dynamicRule) {
        return dynamicRuleRepository.save(dynamicRule);
    }

    /**
     * Получение всех правил
     *
     * @return список всех правил
     */
    public List<DynamicRule> getAllRules() {
        return dynamicRuleRepository.findAll();
    }

    /**
     * Удаление правила по идентификатору продукта
     *
     * @param productId идентификатор продукта
     */
    public void deleteRule(UUID productId) {
        dynamicRuleRepository.deleteByProductId(productId);
    }
}