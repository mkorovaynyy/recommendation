package org.skypro.recommendation.controller.rule;

import org.skypro.recommendation.model.rule.DynamicRule;
import org.skypro.recommendation.repository.rule.DynamicRuleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {
    private final DynamicRuleRepository dynamicRuleRepository;

    public RuleStatsController(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<RuleStatsResponse> getStats() {
        List<DynamicRule> rules = dynamicRuleRepository.findAll();
        List<RuleStat> stats = rules.stream()
                .map(rule -> new RuleStat(rule.getId(), rule.getCounter()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new RuleStatsResponse(stats));
    }

    public static class RuleStatsResponse {
        private List<RuleStat> stats;

        public RuleStatsResponse(List<RuleStat> stats) {
            this.stats = stats;
        }

        public List<RuleStat> getStats() { return stats; }
        public void setStats(List<RuleStat> stats) { this.stats = stats; }
    }

    public static class RuleStat {
        private Long rule_id;
        private Long count;

        public RuleStat(Long ruleId, Long count) {
            this.rule_id = ruleId;
            this.count = count;
        }

        public Long getRule_id() { return rule_id; }
        public void setRule_id(Long rule_id) { this.rule_id = rule_id; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }
}