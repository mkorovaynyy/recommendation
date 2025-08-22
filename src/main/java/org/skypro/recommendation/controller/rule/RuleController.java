package org.skypro.recommendation.controller.rule;

import org.skypro.recommendation.model.rule.DynamicRule;
import org.skypro.recommendation.repository.rule.DynamicRuleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {
    private final DynamicRuleRepository dynamicRuleRepository;

    public RuleController(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    @PostMapping
    public ResponseEntity<DynamicRule> createRule(@RequestBody DynamicRule dynamicRule) {
        DynamicRule saved = dynamicRuleRepository.save(dynamicRule);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<DynamicRule>> getAllRules() {
        return ResponseEntity.ok(dynamicRuleRepository.findAll());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        dynamicRuleRepository.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}