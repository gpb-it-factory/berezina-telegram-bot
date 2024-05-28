package com.sabinaber.berezinabot.adapters.strategy.impl;

import com.sabinaber.berezinabot.adapters.strategy.CommandStrategy;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UserRegistrationCommandStrategy implements CommandStrategy {

    @Override
    public void invoke(String messageText, long chatId, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("registration");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommand() {
        return "/register";
    }
}