package com.sabinaber.berezinabot.adapters.handler;

import com.sabinaber.berezinabot.dto.ExecuteCommandRequestDTO;
import com.sabinaber.berezinabot.dto.ExecuteCommandResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final Set<String> commandSet = new HashSet<>();
    private final WebClient webClient;

    public CommandHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    @Scheduled(fixedRate = 60000) // Попытка загрузки команд каждые 60 секунд
    @Async
    public CompletableFuture<Void> loadCommands() {
        return CompletableFuture.runAsync(() -> {
            String url = "http://localhost:8082/api/commands";
            try {
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
                    logger.info("Loaded commands from middle layer: {}", response.getBody());
                } else {
                    logger.error("Failed to load commands from middle layer");
                }
            } catch (Exception e) {
                logger.error("Error loading commands from middle layer", e);
            }
        });
    }

    public boolean isCommandAvailable(String command) {
        logger.info("Checking if command is available: {}", command);
        logger.info("Available commands: {}", commandSet);
        return commandSet.contains(command);
    }

    public ExecuteCommandResponseDTO forwardToMiddleLayer(String command, String commandData, long chatId) {
        String url = "http://localhost:8082/api/execute";
        ExecuteCommandRequestDTO request = new ExecuteCommandRequestDTO();
        request.setCommand(command);
        request.setCommandData(commandData);
        request.setChatId(chatId);

        try {
            ResponseEntity<ExecuteCommandResponseDTO> response = webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(ExecuteCommandResponseDTO.class)
                    .block();

            if (response != null) {
                logger.info("Response from middle layer: {}", Objects.requireNonNull(response.getBody()).getResponseMessage());
                return response.getBody();
            } else {
                logger.error("Failed to communicate with middle layer");
                ExecuteCommandResponseDTO errorResponse = new ExecuteCommandResponseDTO();
                errorResponse.setResponseMessage("Failed to communicate with middle layer");
                return errorResponse;
            }
        } catch (WebClientResponseException e) {
            logger.error("Error during communication with middle layer", e);
            ExecuteCommandResponseDTO errorResponse = new ExecuteCommandResponseDTO();
            errorResponse.setResponseMessage("Service unavailable. Please try again later.");
            return errorResponse;
        } catch (Exception e) {
            logger.error("Unexpected error during communication with middle layer", e);
            ExecuteCommandResponseDTO errorResponse = new ExecuteCommandResponseDTO();
            errorResponse.setResponseMessage("Service unavailable due to unexpected error. Please try again later.");
            return errorResponse;
        }
    }

    public boolean isMiddleLayerAvailable() {
        String url = "http://localhost:8082/api/commands";
        try {
            ResponseEntity<String> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            return response != null;
        } catch (Exception e) {
            logger.error("Middle layer is unavailable", e);
            return false;
        }
    }
}









