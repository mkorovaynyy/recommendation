package org.skypro.recommendation.model.rule;

import java.util.List;

/**
 * Модель запроса правила
 * Содержит информацию о типе запроса, аргументах и флаге отрицания
 */
public class RuleQuery {
    private String query;
    private List<String> arguments;
    private boolean negate;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Проверка флага отрицания
     *
     * @return true если правило должно быть отрицательным
     */
    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }
}