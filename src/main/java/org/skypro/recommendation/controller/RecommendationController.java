package org.skypro.recommendation.controller;

import org.skypro.recommendation.model.dto.RecommendationResponse;
import org.skypro.recommendation.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Контроллер для обработки запросов рекомендаций
 * Предоставляет endpoint для получения рекомендаций по ID пользователя
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    /**
     * Конструктор контроллера рекомендаций
     *
     * @param recommendationService сервис для получения рекомендаций
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Получение рекомендаций для пользователя
     *
     * @param userId ID пользователя в формате строки
     * @return ответ с рекомендациями
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public RecommendationResponse getRecommendations(@PathVariable String userId) {
        return (RecommendationResponse) recommendationService.getRecommendations(UUID.fromString(userId));
    }
}