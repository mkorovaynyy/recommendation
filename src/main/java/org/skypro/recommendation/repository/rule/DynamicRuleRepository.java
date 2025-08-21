package org.skypro.recommendation.repository.rule;

import org.skypro.recommendation.model.rule.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {
    void deleteByProductId(UUID productId);
}
