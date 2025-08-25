# Развертывание приложения

## Необходимые сервисы

- PostgreSQL 12+ (для базы правил)
- H2 или PostgreSQL (для базы транзакций)
- Telegram Bot Token (для работы бота)

## Сборка приложения

# Сборка JAR-файла

./mvnw clean package

# Запуск тестов

./mvnw test

# Запуск с настройками по умолчанию

java -jar recommendation-1.0.0.jar

# Запуск с кастомными настройками

java -jar recommendation-1.0.0.jar \
--spring.datasource.url=jdbc:postgresql://localhost:5432/transactions \
--spring.datasource.username=user \
--spring.datasource.password=pass \
--spring.datasource.secondary.jdbc-url=jdbc:postgresql://localhost:5432/rules \
--spring.datasource.secondary.username=user \
--spring.datasource.secondary.password=pass \
--telegram.bot.token=your_bot_token \
--telegram.bot.name=your_bot_name

SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/transactions
SPRING_DATASOURCE_USERNAME: user
SPRING_DATASOURCE_PASSWORD: pass
SPRING_DATASOURCE_SECONDARY_JDBC_URL: jdbc:postgresql://localhost:5432/rules
SPRING_DATASOURCE_SECONDARY_USERNAME: user
SPRING_DATASOURCE_SECONDARY_PASSWORD: pass
TELEGRAM_BOT_TOKEN: 123456:ABC-DEF1234
TELEGRAM_BOT_NAME: MyRecommendationBot
SERVER_PORT: 8080