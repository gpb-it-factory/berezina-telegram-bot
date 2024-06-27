package com.sabinaber.berezinabot.dto;

public class ExecuteCommandRequestDTO {
    private String command;
    private String commandData;
    private long chatId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandData() {
        return commandData;
    }

    public void setCommandData(String commandData) {
        this.commandData = commandData;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}