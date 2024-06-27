package com.sabinaber.berezinabot.adapters.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Component
public class CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final Set<String> commandSet = new HashSet<>();
    private final WebClient webClient;

    public CommandHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    public void loadCommands() {
        String url = "http://localhost:8082/api/commands";
//        webClient.get()
//                .uri(url)
//                .retrieve()
//                .bodyToFlux(String.class)
//                .doOnNext(command -> commandSet.addAll(Arrays.asList(command.split(","))))
//                .doOnComplete(() -> logger.info("Loaded commands from middle layer: {}", commandSet))
//                .doOnError(e -> logger.error("Failed to load commands from middle layer", e))
//                .subscribe();
//        Flux<String> responseFlux = webClient.get()
//                .uri(url) // замените на ваш URL
//                .retrieve()
//                .bodyToFlux(String.class);
//
//        responseFlux.subscribe(command -> {
//            commandSet.add(command);
//            logger.info("Loaded command: {}", command);
//        }, e -> {
//            logger.error("Failed to communicate with middle layer", e);
//        }, () -> {
//            logger.info("Loaded commands from middle layer: {}", commandSet);
//        });
//        Mono<List<List<String>>> responseMono = webClient.get()
//                .uri(url) // замените на ваш URL
//                .retrieve()
//                .bodyToMono((new ParameterizedTypeReference<List<List<String>>>() {}));
//
//        responseMono.subscribe(response -> {
//            response.forEach(innerList -> commandSet.addAll(innerList));
//            logger.info("Loaded commands from middle layer: {}", commandSet);
//        }, e -> {
//            logger.error("Failed to communicate with middle layer", e);
//        });


        ResponseEntity<String> response = webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class)
                .block();
        if (response != null) {
            String responseBody = response.getBody().substring(1, response.getBody().length() - 1);
            String[] commands = responseBody.split(",");
            for (String command : commands) {
                logger.info("Command: {}", command);
                String cleanCommand = command.substring(1, command.length() - 1);
                commandSet.add(cleanCommand);
            }
            commandSet.add("/ping");
            logger.info("Loaded commands from middle layer: {}", response.getBody());
        } else {
            logger.error("Failed to load commands from middle layer");
        }
    }

    public boolean isCommandAvailable(String command) {
        logger.info("Checking if command is available: {}", command);
        logger.info("Available commands: {}", commandSet);
        logger.info("Check bool: {}", commandSet.contains(command));
        return commandSet.contains(command);
    }


//    public void handleCommand(MessageHandler message, TelegramLongPollingBot bot) {
//        logger.info("Handling command: {} with data: {} for chatId: {}, username: {}", message.getCommand(), message.getCommandData(), message.getChatId(), message.getUserName());
//
//        CommandStrategy strategy = strategies.get(message.getCommand());
//
//        if (strategy != null) {
//            logger.info("Found strategy for command: {}", message.getCommand());
//            strategy.invoke(message.getCommandData(), message.getChatId(), bot);
//        } else {
//            logger.warn("No strategy found for command: {}. Forwarding to middle layer.", message.getCommand());
//            forwardToMiddleLayer(message.getCommand(), message.getCommandData(), message.getChatId(), bot);
//        }
//    }

    public ResponseEntity<String> forwardToMiddleLayer(MessageHandler message) {
        String url = "http://localhost:8082/api/execute";
        Map<String, String> params = new HashMap<>();
        params.put("command", message.getCommand());
        params.put("commandData", message.getCommandData());
        params.put("chatId", String.valueOf(message.getChatId()));

        ResponseEntity<String> response = webClient.post()
                .uri(url)
                .bodyValue(params)
                .retrieve()
                .toEntity(String.class)
                .block();
        if (response != null) {
            logger.info("Response from middle layer: {}", response.getBody());
        } else {
            logger.error("Failed to communicate with middle layer");
        }
        return response;
//        ResponseEntity<String> result = webClient.post()
//                .uri(url)
//                .bodyValue(params)
//                .retrieve()
//                .bodyToMono(String.class)
//                .doOnNext(response -> logger.info("Response from middle layer: {}", response))
//                .doOnError(e -> {
//                    logger.error("Failed to communicate with middle layer", e);
//                })
//                .block();
    }

}








