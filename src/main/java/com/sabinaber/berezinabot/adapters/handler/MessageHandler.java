package com.sabinaber.berezinabot.adapters.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

public class MessageHandler {
    private final String command;
    private final String commandData;
    private final long chatId;
    private final String userName;

    public MessageHandler(Message message) {
        this.userName = message.getChat().getUserName();
        String messageText = message.getText();
        this.command = extractCommand(messageText);
        this.commandData = extractData(messageText);
        this.chatId = message.getChatId();
    }

    private String extractCommand(String messageText) {
        if (messageText.startsWith("/")) {
            return messageText.split(" ")[0];
        }
        return null;
    }


    private String extractData(String messageText) {
        if (messageText.startsWith("/")) {
            String[] parts = messageText.split(" ", 2);
            return parts.length > 1 ? parts[1] : "";
        }
        return messageText;
    }

    public boolean isCommand() {
        return command != null;
    }

    public String getCommand() {
        return command;
    }

    public long getChatId() {
        return chatId;
    }

    public String getCommandData() {
        return commandData;
    }

    public String getUserName() {
        return userName;
    }
}
