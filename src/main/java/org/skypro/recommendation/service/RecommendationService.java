package org.skypro.recommendation.service;

import org.skypro.recommendation.model.dto.Recommendation;
import org.skypro.recommendation.model.entity.Product;
import org.skypro.recommendation.repository.ProductRepository;
import org.skypro.recommendation.service.rule.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final Map<String, List<Function<UUID, Boolean>>> productRules = new HashMap<>();
    private final ProductRepository productRepository;
    private final DebitRule debitRule;
    private final InvestRule investRule;
    private final SavingRule savingRule;
    private final CreditRule creditRule;
    private final TransactionRule transactionRule;

    public RecommendationService(
            ProductRepository productRepository,
            DebitRule debitRule,
            InvestRule investRule,
            SavingRule savingRule,
            CreditRule creditRule,
            TransactionRule transactionRule
    ) {
        this.productRepository = productRepository;
        this.debitRule = debitRule;
        this.investRule = investRule;
        this.savingRule = savingRule;
        this.creditRule = creditRule;
        this.transactionRule = transactionRule;
        initRules();
    }

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

    public List<Recommendation> getRecommendations(UUID userId) {
        return productRepository.getAllProducts().stream()
                .filter(product -> productRules.containsKey(product.getId().toString()))
                .filter(product -> matchesAllRules(userId, product.getId().toString()))
                .map(product -> new Recommendation(
                        product.getName(),
                        product.getId().toString(),
                        product.getDescription()))
                .collect(Collectors.toList());
    }

    private boolean matchesAllRules(UUID userId, String productId) {
        List<Function<UUID, Boolean>> rules = productRules.get(productId);
        if (rules == null) return false;

        return rules.stream().allMatch(rule -> rule.apply(userId));
    }
}