package com.ebank.backend.chatbot.config;

import com.ebank.backend.chatbot.services.ChatbotService;
import com.ebank.backend.chatbot.telegram.EbankTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramBotConfig {

    @Value("${telegram.bot.token:}")
    private String botToken;

    @Value("${telegram.bot.username:}")
    private String botUsername;

    private final ApplicationContext applicationContext;

    public TelegramBotConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Registers the Telegram bot once the app is fully up, and only if both
     * TELEGRAM_BOT_TOKEN and TELEGRAM_BOT_USERNAME are set. This keeps
     * `mvn spring-boot:run` working even for people who haven't set up a
     * Telegram bot yet (Parts 1-3 don't need it).
     */
    @EventListener(ApplicationReadyEvent.class)
    public void registerBot() {
        if (botToken == null || botToken.isBlank() || botUsername == null || botUsername.isBlank()) {
            log.warn("TELEGRAM_BOT_TOKEN / TELEGRAM_BOT_USERNAME not set - Telegram bot not started.");
            return;
        }

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            ChatbotService chatbotService = applicationContext.getBean(ChatbotService.class);
            telegramBotsApi.registerBot(new EbankTelegramBot(botToken, botUsername, chatbotService));
            log.info("Telegram bot '{}' registered and polling.", botUsername);
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram bot: {}", e.getMessage());
        }
    }
}
