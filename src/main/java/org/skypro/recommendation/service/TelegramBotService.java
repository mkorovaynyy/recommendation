package org.skypro.recommendation.service;

import org.skypro.recommendation.model.dto.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.UUID;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final RecommendationService recommendationService;
    private final UserService userService;
    private final String botUsername;

    @Autowired
    public TelegramBotService(RecommendationService recommendationService,
                              UserService userService,
                              @Qualifier("getBotName") String botToken,
                              @Qualifier("getBotToken") String botName) {
        super(botToken);
        this.recommendationService = recommendationService;
        this.userService = userService;
        this.botUsername = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/recommend ")) {
                String username = messageText.substring("/recommend ".length()).trim();
                handleRecommendCommand(chatId, username);
            } else if (messageText.equals("/start")) {
                sendHelpMessage(chatId);
            }
        }
    }

    private void handleRecommendCommand(Long chatId, String username) {
        UUID userId = userService.findUserIdByUsername(username);
        if (userId == null) {
            sendMessage(chatId, "Пользователь не найден");
            return;
        }

        List<Recommendation> recommendations = recommendationService.getRecommendations(userId);
        String response = formatRecommendations(username, recommendations);
        sendMessage(chatId, response);
    }

    private String formatRecommendations(String username, List<Recommendation> recommendations) {
        StringBuilder sb = new StringBuilder();
        sb.append("Здравствуйте ").append(username).append("\n\n");
        sb.append("Новые продукты для вас:\n");

        if (recommendations.isEmpty()) {
            sb.append("На данный момент рекомендаций нет.");
        } else {
            for (Recommendation rec : recommendations) {
                sb.append("• ").append(rec.getName()).append(": ").append(rec.getText()).append("\n");
            }
        }

        return sb.toString();
    }

    private void sendHelpMessage(Long chatId) {
        String helpText = "Привет! Я бот для рекомендаций продуктов.\n\n" +
                "Используйте команду /recommend username для получения рекомендаций";
        sendMessage(chatId, helpText);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}