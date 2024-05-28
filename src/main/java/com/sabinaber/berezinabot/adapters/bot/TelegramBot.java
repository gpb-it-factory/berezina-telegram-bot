package com.sabinaber.berezinabot.adapters.bot;

import com.sabinaber.berezinabot.adapters.handler.CommandHandler;
import com.sabinaber.berezinabot.adapters.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final CommandHandler commandHandler;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       @Value("${telegram.bot.username}") String botUsername,
                       DefaultBotOptions options,
                       CommandHandler commandHandler) {
        super(options);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.commandHandler = commandHandler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            MessageHandler messageHandler = new MessageHandler(update.getMessage());
            logger.info("Received message: {} from chatId: {}", update.getMessage().getText(), messageHandler.getChatId());
            if (messageHandler.isCommand()) {
                commandHandler.handleCommand(messageHandler, this);
            } else {
                handleTextMessage(messageHandler.getCommandData(), messageHandler.getChatId());
            }
        }
    }

    private void handleTextMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Received text message: " + text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Failed to send text message response", e);
        }
    }
}



