package org.skypro.recommendation.controller;

import org.skypro.recommendation.model.dto.Recommendation;
import org.skypro.recommendation.model.dto.RecommendationResponse;
import org.skypro.recommendation.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @PathVariable String userId) {
        try {
            UUID uuid = UUID.fromString(userId);
            List<Recommendation> recommendations = recommendationService.getRecommendations(uuid);
            RecommendationResponse response = new RecommendationResponse(userId, recommendations);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
