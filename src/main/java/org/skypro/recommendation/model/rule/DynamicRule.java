package org.skypro.recommendation.model.rule;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rule")
public class DynamicRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_text")
    private String productText;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<RuleQuery> rule;

    @Column(name = "counter")
    private Long counter = 0L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }
    public List<RuleQuery> getRule() { return rule; }
    public void setRule(List<RuleQuery> rule) { this.rule = rule; }
    public Long getCounter() { return counter; }
    public void setCounter(Long counter) { this.counter = counter; }
}