package com.sabinaber.berezinabot.adapters.strategy;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public interface CommandStrategy {
    void invoke(String messageText, long chatId, TelegramLongPollingBot bot);
    String getCommand();
}

