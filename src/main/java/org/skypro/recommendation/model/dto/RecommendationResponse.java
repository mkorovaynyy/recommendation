package org.skypro.recommendation.model.dto;

import java.util.List;

/**
 * DTO для ответа с рекомендациями
 * Содержит ID пользователя и список рекомендаций
 */
public class RecommendationResponse {
    private String userId;
    private List<Recommendation> recommendations;

    /**
     * Конструктор по умолчанию
     */
    public RecommendationResponse() {
    }

    /**
     * Конструктор с параметрами
     *
     * @param userId          ID пользователя
     * @param recommendations список рекомендаций
     */
    public RecommendationResponse(String userId, List<Recommendation> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}