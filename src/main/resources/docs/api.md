openapi: 3.0.0
info:
title: Recommendation Service API
description: API для системы рекомендаций финансовых продуктов
version: 1.0.0

servers:
- url: http://localhost:8080
  description: Development server

paths:
/recommendation/{userId}:
get:
summary: Получить рекомендации для пользователя
description: Возвращает список рекомендованных финансовых продуктов для указанного пользователя
parameters:
- name: userId
in: path
required: true
description: UUID пользователя
schema:
type: string
format: uuid
example: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
responses:
'200':
description: Успешный ответ с рекомендациями
content:
application/json:
schema:
$ref: '#/components/schemas/RecommendationResponse'
'400':
description: Неверный формат UUID пользователя
'500':
description: Внутренняя ошибка сервера

/rule:
post:
summary: Создать новое правило
description: Создает новое динамическое правило для рекомендаций
requestBody:
required: true
content:
application/json:
schema:
$ref: '#/components/schemas/DynamicRule'
responses:
'201':
description: Правило успешно создано
content:
application/json:
schema:
$ref: '#/components/schemas/DynamicRule'
'400':
description: Неверные данные правила
'500':
description: Внутренняя ошибка сервера
get:
summary: Получить все правила
description: Возвращает список всех динамических правил
responses:
'200':
description: Успешный ответ со списком правил
content:
application/json:
schema:
type: array
items:
$ref: '#/components/schemas/DynamicRule'
'500':
description: Внутренняя ошибка сервера

/rule/{productId}:
delete:
summary: Удалить правило по ID продукта
description: Удаляет динамическое правило, связанное с указанным продуктом
parameters:
- name: productId
in: path
required: true
description: UUID продукта
schema:
type: string
format: uuid
example: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
responses:
'204':
description: Правило успешно удалено
'404':
description: Правило для указанного продукта не найдено
'500':
description: Внутренняя ошибка сервера

/rule/stats:
get:
summary: Получить статистику правил
description: Возвращает статистику срабатываний всех динамических правил
responses:
'200':
description: Успешный ответ со статистикой
content:
application/json:
schema:
$ref: '#/components/schemas/RuleStatsResponse'
'500':
description: Внутренняя ошибка сервера

/management/clear-caches:
post:
summary: Очистить кэши
description: Очищает все кэши приложения
responses:
'200':
description: Кэши успешно очищены
'500':
description: Внутренняя ошибка сервера

/management/info:
get:
summary: Получить информацию о сервисе
description: Возвращает основную информацию о сервисе (название и версию)
responses:
'200':
description: Успешный ответ с информацией о сервисе
content:
application/json:
schema:
type: object
properties:
name:
type: string
example: "recommendation-service"
version:
type: string
example: "1.0.0"
'500':
description: Внутренняя ошибка сервера

components:
schemas:
RecommendationResponse:
type: object
properties:
userId:
type: string
format: uuid
example: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
recommendations:
type: array
items:
$ref: '#/components/schemas/Recommendation'

    Recommendation:
      type: object
      properties:
        name:
          type: string
          example: "Инвестиционный продукт 'Старт'"
        id:
          type: string
          format: uuid
          example: "147f6a0f-3b91-413b-ab99-87f081d60d5a"
        text:
          type: string
          example: "Идеально подходит для начала инвестирования"

    DynamicRule:
      type: object
      properties:
        id:
          type: integer
          example: 1
        productName:
          type: string
          example: "Премиальная кредитная карта"
        productId:
          type: string
          format: uuid
          example: "ab138afb-f3ba-4a93-b74f-0fcee86d447f"
        productText:
          type: string
          example: "Кредитная карта с повышенным кэшбэком"
        rule:
          type: array
          items:
            $ref: '#/components/schemas/RuleQuery'
        counter:
          type: integer
          example: 42

    RuleQuery:
      type: object
      properties:
        query:
          type: string
          example: "USER_OF"
        arguments:
          type: array
          items:
            type: string
          example: ["DEBIT"]
        negate:
          type: boolean
          example: false

    RuleStatsResponse:
      type: object
      properties:
        stats:
          type: array
          items:
            $ref: '#/components/schemas/RuleStat'

    RuleStat:
      type: object
      properties:
        rule_id:
          type: integer
          example: 1
        count:
          type: integer
          example: 42