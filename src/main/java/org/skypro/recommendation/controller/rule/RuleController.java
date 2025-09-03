package org.skypro.recommendation.controller.rule;

import org.skypro.recommendation.model.rule.DynamicRule;
import org.skypro.recommendation.service.RuleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления правилами рекомендаций
 * Предоставляет endpoints для CRUD операций с правилами
 */
@RestController
@RequestMapping("/rule")
public class RuleController {
    private final RuleService ruleService;

    /**
     * Конструктор контроллера правил
     *
     * @param ruleService сервис для работы с правилами
     */
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * Создание нового правила
     *
     * @param dynamicRule правило для создания
     * @return созданное правило
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DynamicRule createRule(@RequestBody DynamicRule dynamicRule) {
        return ruleService.createRule(dynamicRule);
    }

    /**
     * Получение всех правил
     *
     * @return список всех правил
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DynamicRule> getAllRules() {
        return ruleService.getAllRules();
    }

    /**
     * Удаление правила по идентификатору продукта
     *
     * @param productId идентификатор продукта
     */
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable UUID productId) {
        ruleService.deleteRule(productId);
    }
}