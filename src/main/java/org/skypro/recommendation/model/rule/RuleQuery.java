package org.skypro.recommendation.model.rule;

import java.util.List;

public class RuleQuery {
    private String query;
    private List<String> arguments;
    private boolean negate;

    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public List<String> getArguments() { return arguments; }
    public void setArguments(List<String> arguments) { this.arguments = arguments; }
    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }
}