package org.skypro.recommendation.controller.rule;

import org.skypro.recommendation.service.RuleStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Контроллер для получения статистики правил
 * Предоставляет endpoint для получения статистики срабатываний правил рекомендаций
 */
@RestController
@RequestMapping("/rule")
public class RuleStatsController {
    private final RuleStatsService ruleStatsService;

    /**
     * Конструктор контроллера статистики правил
     *
     * @param ruleStatsService сервис для работы со статистикой правил
     */
    public RuleStatsController(RuleStatsService ruleStatsService) {
        this.ruleStatsService = ruleStatsService;
    }

    /**
     * Получение статистики срабатываний правил
     *
     * @return статистика срабатываний всех правил
     */
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public RuleStatsResponse getStats() {
        return ruleStatsService.getStats();
    }

    public static class RuleStatsResponse {
        private List<RuleStat> stats;

        public RuleStatsResponse(List<RuleStat> stats) {
            this.stats = stats;
        }

        public List<RuleStat> getStats() {
            return stats;
        }

        public void setStats(List<RuleStat> stats) {
            this.stats = stats;
        }
    }

    public static class RuleStat {
        private Long rule_id;
        private Long count;

        public RuleStat(Long ruleId, Long count) {
            this.rule_id = ruleId;
            this.count = count;
        }

        public Long getRule_id() {
            return rule_id;
        }

        public void setRule_id(Long rule_id) {
            this.rule_id = rule_id;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }
}