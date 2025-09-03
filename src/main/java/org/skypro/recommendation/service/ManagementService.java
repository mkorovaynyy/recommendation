package org.skypro.recommendation.service;

import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для управления сервисом
 * Содержит бизнес-логику для операций управления
 */
@Service
public class ManagementService {
    private final CacheManager cacheManager;
    private final BuildProperties buildProperties;

    /**
     * Конструктор сервиса управления
     *
     * @param cacheManager менеджер кэша
     * @param buildProperties свойства сборки
     */
    public ManagementService(CacheManager cacheManager, BuildProperties buildProperties) {
        this.cacheManager = cacheManager;
        this.buildProperties = buildProperties;
    }

    /**
     * Очистка всех кэшей
     */
    public void clearCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            if (cacheManager.getCache(name) != null) {
                cacheManager.getCache(name).clear();
            }
        });
    }

    /**
     * Получение информации о сервисе
     *
     * @return информация о названии и версии сервиса
     */
    public Map<String, String> getInfo() {
        return Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        );
    }
}
