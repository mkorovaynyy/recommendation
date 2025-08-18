package org.skypro.recommendation.service.rule;

import java.util.UUID;

public interface RecommendationRule {
    boolean evaluate(UUID userId);
}
