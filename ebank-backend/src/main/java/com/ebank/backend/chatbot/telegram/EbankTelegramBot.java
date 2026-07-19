package com.ebank.backend.chatbot.telegram;

import com.ebank.backend.chatbot.services.ChatbotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Telegram front-end for the same RAG chatbot exposed at POST /chatbot/ask.
 * Requires TELEGRAM_BOT_TOKEN and TELEGRAM_BOT_USERNAME as environment
 * variables - if unset, the bot bean still starts but registration in
 * TelegramBotConfig is skipped (see that class).
 */
@Slf4j
public class EbankTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final ChatbotService chatbotService;

    public EbankTelegramBot(String botToken, String botUsername, ChatbotService chatbotService) {
        super(botToken);
        this.botUsername = botUsername;
        this.chatbotService = chatbotService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userText = update.getMessage().getText();

            String answer;
            try {
                answer = chatbotService.ask(userText);
            } catch (Exception e) {
                log.error("Chatbot call failed for Telegram message: {}", e.getMessage());
                answer = "Sorry, I couldn't process that right now.";
            }

            SendMessage message = new SendMessage(chatId, answer);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Failed to send Telegram reply: {}", e.getMessage());
            }
        }
    }
}
