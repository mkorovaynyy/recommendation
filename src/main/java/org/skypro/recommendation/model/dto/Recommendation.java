package org.skypro.recommendation.model.dto;

/**
 * DTO для представления рекомендации
 * Содержит информацию о рекомендуемом продукте
 */
public class Recommendation {
    private String name;
    private String id;
    private String text;

    /**
     * Конструктор по умолчанию
     */
    public Recommendation() {
    }

    /**
     * Конструктор с параметрами
     *
     * @param name название продукта
     * @param id   идентификатор продукта
     * @param text описание рекомендации
     */
    public Recommendation(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}