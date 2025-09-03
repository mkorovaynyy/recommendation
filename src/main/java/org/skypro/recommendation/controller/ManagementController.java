package org.skypro.recommendation.controller;

import org.skypro.recommendation.service.ManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для управления сервисом
 * Предоставляет endpoints для сброса кэша и получения информации о сервисе
 */
@RestController
@RequestMapping("/management")
public class ManagementController {
    private final ManagementService managementService;

    /**
     * Конструктор контроллера управления
     *
     * @param managementService сервис для операций управления
     */
    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    /**
     * Сброс всех кэшей
     */
    @PostMapping("/clear-caches")
    @ResponseStatus(HttpStatus.OK)
    public void clearCaches() {
        managementService.clearCaches();
    }

    /**
     * Получение информации о сервисе
     *
     * @return информация о названии и версии сервиса
     */
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getInfo() {
        return managementService.getInfo();
    }
}