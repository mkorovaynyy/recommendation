package org.skypro.recommendation.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для управления сервисом
 * Предоставляет endpoints для сброса кэша и получения информации о сервисе
 */
@RestController
@RequestMapping("/management")
public class ManagementController {
    private final CacheManager cacheManager;
    private final BuildProperties buildProperties;

    /**
     * Конструктор контроллера управления
     *
     * @param cacheManager    менеджер кэша
     * @param buildProperties свойства сборки
     */
    public ManagementController(CacheManager cacheManager, BuildProperties buildProperties) {
        this.cacheManager = cacheManager;
        this.buildProperties = buildProperties;
    }

    /**
     * Сброс всех кэшей
     *
     * @return ResponseEntity с статусом OK
     */
    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            if (cacheManager.getCache(name) != null) {
                cacheManager.getCache(name).clear();
            }
        });
        return ResponseEntity.ok().build();
    }

    /**
     * Получение информации о сервисе
     *
     * @return информация о названии и версии сервиса
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity.ok(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}