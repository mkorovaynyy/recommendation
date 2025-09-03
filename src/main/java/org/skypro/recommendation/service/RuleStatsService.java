package org.skypro.recommendation.service;

import org.skypro.recommendation.controller.rule.RuleStatsController;
import org.skypro.recommendation.model.rule.DynamicRule;
import org.skypro.recommendation.repository.rule.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы со статистикой правил
 * Содержит бизнес-логику для операций со статистикой
 */
@Service
public class RuleStatsService {
    private final DynamicRuleRepository dynamicRuleRepository;

    /**
     * Конструктор сервиса статистики правил
     *
     * @param dynamicRuleRepository репозиторий динамических правил
     */
    public RuleStatsService(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    /**
     * Получение статистики срабатываний правил
     *
     * @return ответ со статистикой правил
     */
    public RuleStatsController.RuleStatsResponse getStats() {
        List<DynamicRule> rules = dynamicRuleRepository.findAll();
        List<RuleStatsController.RuleStat> stats = rules.stream()
                .map(rule -> new RuleStatsController.RuleStat(rule.getId(), rule.getCounter()))
                .collect(Collectors.toList());

        return new RuleStatsController.RuleStatsResponse(stats);
    }
}