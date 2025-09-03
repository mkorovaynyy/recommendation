package org.skypro.recommendation.service;

import org.skypro.recommendation.exception.InvalidUserIdException;
import org.skypro.recommendation.model.dto.Recommendation;
import org.skypro.recommendation.model.dto.RecommendationResponse;
import org.skypro.recommendation.model.rule.DynamicRule;
import org.skypro.recommendation.model.rule.RuleQuery;
import org.skypro.recommendation.repository.ProductRepository;
import org.skypro.recommendation.repository.rule.DynamicRuleRepository;
import org.skypro.recommendation.service.rule.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис для формирования рекомендаций
 * Содержит логику обработки статических и динамических правил рекомендаций
 */
@Service
public class RecommendationService {
    private final Map<String, List<Function<UUID, Boolean>>> productRules = new HashMap<>();
    private final ProductRepository productRepository;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final KnowledgeBaseService knowledgeBaseService;
    private final DebitRule debitRule;
    private final InvestRule investRule;
    private final SavingRule savingRule;
    private final CreditRule creditRule;
    private final TransactionRule transactionRule;

    /**
     * Конструктор сервиса рекомендаций
     *
     * @param productRepository     репозиторий продуктов
     * @param dynamicRuleRepository репозиторий динамических правил
     * @param knowledgeBaseService  сервис базы знаний
     * @param debitRule             правило для дебетовых продуктов
     * @param investRule            правило для инвестиционных продуктов
     * @param savingRule            правило для сберегательных продуктов
     * @param creditRule            правило для кредитных продуктов
     * @param transactionRule       правило для проверки транзакций
     */
    public RecommendationService(
            ProductRepository productRepository,
            DynamicRuleRepository dynamicRuleRepository,
            KnowledgeBaseService knowledgeBaseService,
            DebitRule debitRule,
            InvestRule investRule,
            SavingRule savingRule,
            CreditRule creditRule,
            TransactionRule transactionRule
    ) {
        this.productRepository = productRepository;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.knowledgeBaseService = knowledgeBaseService;
        this.debitRule = debitRule;
        this.investRule = investRule;
        this.savingRule = savingRule;
        this.creditRule = creditRule;
        this.transactionRule = transactionRule;
        initRules();
    }

    /**
     * Инициализация статических правил рекомендаций
     */
    private void initRules() {
        // Invest 500
        productRules.put("147f6a0f-3b91-413b-ab99-87f081d60d5a", Arrays.asList(
                debitRule::evaluate,
                userId -> !investRule.evaluate(userId),
                userId -> savingRule.evaluateWithMinAmount(userId, 1000)
        ));

        // Top Saving
        productRules.put("59efc529-2fff-41af-baff-90ccd7402925", Arrays.asList(
                debitRule::evaluate,
                userId -> transactionRule.minTotalDeposit(userId, 50000),
                transactionRule::debitDepositExceedsWithdrawal
        ));

        // Простой кредит
        productRules.put("ab138afb-f3ba-4a93-b74f-0fcee86d447f", Arrays.asList(
                userId -> !creditRule.evaluate(userId),
                transactionRule::debitDepositExceedsWithdrawal,
                userId -> transactionRule.minTotalWithdrawal(userId, 100000)
        ));
    }

    /**
     * Получение рекомендаций для пользователя
     *
     * @param userId идентификатор пользователя
     * @return список рекомендаций
     */
    public List<Recommendation> getRecommendations(UUID userId) {
        List<Recommendation> recommendations = new ArrayList<>();

        // Статические правила
        recommendations.addAll(getStaticRecommendations(userId));

        // Динамические правила
        recommendations.addAll(getDynamicRecommendations(userId));

        return recommendations;
    }

    /**
     * Получение рекомендаций по статическим правилам
     *
     * @param userId идентификатор пользователя
     * @return список рекомендаций по статическим правилам
     */
    private List<Recommendation> getStaticRecommendations(UUID userId) {
        return productRepository.getAllProducts().stream()
                .filter(product -> productRules.containsKey(product.getId().toString()))
                .filter(product -> matchesAllRules(userId, product.getId().toString()))
                .map(product -> new Recommendation(
                        product.getName(),
                        product.getId().toString(),
                        product.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * Получение рекомендаций по динамическим правилам
     *
     * @param userId идентификатор пользователя
     * @return список рекомендаций по динамическим правилам
     */
    private List<Recommendation> getDynamicRecommendations(UUID userId) {
        return dynamicRuleRepository.findAll().stream()
                .filter(rule -> {
                    boolean matches = matchesDynamicRule(userId, rule);
                    if (matches) {
                        rule.setCounter(rule.getCounter() + 1);
                        dynamicRuleRepository.save(rule);
                    }
                    return matches;
                })
                .map(rule -> new Recommendation(
                        rule.getProductName(),
                        rule.getProductId().toString(),
                        rule.getProductText()))
                .collect(Collectors.toList());
    }

    /**
     * Проверка соответствия пользователя динамическому правилу
     *
     * @param userId идентификатор пользователя
     * @param rule   динамическое правило
     * @return true если пользователь соответствует правилу
     */
    private boolean matchesDynamicRule(UUID userId, DynamicRule rule) {
        for (RuleQuery query : rule.getRule()) {
            boolean result = evaluateQuery(userId, query);
            if (query.isNegate()) {
                result = !result;
            }
            if (!result) {
                return false;
            }
        }
        return true;
    }

    /**
     * Выполнение запроса правила для пользователя
     *
     * @param userId идентификатор пользователя
     * @param query  запрос правила
     * @return результат выполнения запроса
     */
    private boolean evaluateQuery(UUID userId, RuleQuery query) {
        switch (query.getQuery()) {
            case "USER_OF":
                return knowledgeBaseService.userOf(
                        query.getArguments().get(0), userId);
            case "ACTIVE_USER_OF":
                return knowledgeBaseService.activeUserOf(
                        query.getArguments().get(0), userId);
            case "TRANSACTION_SUM_COMPARE":
                return knowledgeBaseService.transactionSumCompare(
                        query.getArguments().get(0),
                        query.getArguments().get(1),
                        query.getArguments().get(2),
                        Integer.parseInt(query.getArguments().get(3)),
                        userId);
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                return knowledgeBaseService.transactionSumCompareDepositWithdraw(
                        query.getArguments().get(0),
                        query.getArguments().get(1),
                        userId);
            default:
                return false;
        }
    }

    /**
     * Проверка соответствия пользователя всем правилам продукта
     *
     * @param userId    идентификатор пользователя
     * @param productId идентификатор продукта
     * @return true если пользователь соответствует всем правилам
     */
    private boolean matchesAllRules(UUID userId, String productId) {
        List<Function<UUID, Boolean>> rules = productRules.get(productId);
        if (rules == null) return false;
        return rules.stream().allMatch(rule -> rule.apply(userId));
    }
    /**
     * Получение рекомендаций для пользователя (публичный метод для контроллера)
     *
     * @param userId идентификатор пользователя в формате строки
     * @return ответ с рекомендациями
     * @throws InvalidUserIdException если передан некорректный формат ID пользователя
     */
    public RecommendationResponse getRecommendations(String userId) {
        try {
            UUID uuid = UUID.fromString(userId);
            List<Recommendation> recommendations = getRecommendations(uuid);
            return new RecommendationResponse(userId, recommendations);
        } catch (IllegalArgumentException e) {
            throw new InvalidUserIdException("Invalid user ID format: " + userId);
        }
    }
}