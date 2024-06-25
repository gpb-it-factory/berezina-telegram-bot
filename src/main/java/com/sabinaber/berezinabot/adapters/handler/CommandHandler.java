package com.sabinaber.berezinabot.adapters.handler;

import com.sabinaber.berezinabot.adapters.strategy.CommandStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final Map<String, CommandStrategy> strategies = new HashMap<>();
    private final WebClient webClient;

    public CommandHandler(List<CommandStrategy> strategyList, WebClient webClient) {
        for (CommandStrategy strategy : strategyList) {
            strategies.put(strategy.getCommand(), strategy);
        }
        this.webClient = webClient;
    }

    public void handleCommand(MessageHandler message, TelegramLongPollingBot bot) {
        logger.info("Handling command: {} with data: {} for chatId: {}, username: {}", message.getCommand(), message.getCommandData(), message.getChatId(), message.getUserName());

        CommandStrategy strategy = strategies.get(message.getCommand());

        if (strategy != null) {
            logger.info("Found strategy for command: {}", message.getCommand());
            strategy.invoke(message.getCommandData(), message.getChatId(), bot);
        } else {
            logger.warn("No strategy found for command: {}. Forwarding to middle layer.", message.getCommand());
            forwardToMiddleLayer(message.getCommand(), message.getCommandData(), message.getChatId(), bot);
        }
    }

    private void forwardToMiddleLayer(String command, String commandData, long chatId, TelegramLongPollingBot bot) {
        String url = "http://localhost:8080/api/execute";
        Map<String, String> params = new HashMap<>();
        params.put("command", command);
        params.put("commandData", commandData);
        params.put("chatId", String.valueOf(chatId));

        webClient.post()
                .uri(url)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Response from middle layer: {}", response))
                .doOnError(e -> {
                    logger.error("Failed to communicate with middle layer", e);
                    sendUnknownCommandMessage(chatId, bot);
                })
                .subscribe();
    }

    private void sendUnknownCommandMessage(long chatId, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Unknown command. Please use a valid command.");

        try {
            bot.execute(message);
            logger.info("Sent unknown command message to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send unknown command message", e);
        }
    }
}








