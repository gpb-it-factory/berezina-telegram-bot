package com.sabinaber.berezinabot.adapters.bot;

import com.sabinaber.berezinabot.adapters.handler.CommandHandler;
import com.sabinaber.berezinabot.adapters.handler.MessageHandler;
import com.sabinaber.berezinabot.dto.ExecuteCommandResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

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

    @PostConstruct
    @Async
    public void init() {
        logger.info("Initializing Telegram bot...");
        if (!commandHandler.isMiddleLayerAvailable()) {
            logger.error("Middle layer service is unavailable during initialization");
        }
        commandHandler.loadCommands();
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
            long chatId = messageHandler.getChatId();
            logger.info("Received message: {} from chatId: {}", messageHandler.getCommand(), chatId);

            if (!commandHandler.isMiddleLayerAvailable()) {
                sendServiceUnavailableMessage(chatId);
                return;
            }

            if (commandHandler.isCommandAvailable(messageHandler.getCommand())) {
                ExecuteCommandResponseDTO response = commandHandler.forwardToMiddleLayer(
                        messageHandler.getCommand(),
                        messageHandler.getCommandData(),
                        chatId);
                handleTextMessage(response.getResponseMessage(), chatId);
            } else {
                sendUnknownCommandMessage(chatId);
            }
        }
    }

    private void sendUnknownCommandMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Unknown command");
        try {
            execute(message);
            logger.info("Sent unknown command message to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send unknown command message", e);
        }
    }

    private void handleTextMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
            logger.info("Sent text message to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send text message response", e);
        }
    }

    private void sendServiceUnavailableMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Service unavailable. Please try again later.");
        try {
            execute(message);
            logger.info("Sent service unavailable message to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send service unavailable message", e);
        }
    }
}





