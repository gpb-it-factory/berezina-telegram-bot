package com.sabinaber.berezinabot.adapters.strategy;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PingCommandStrategy implements CommandStrategy {

    private final TelegramLongPollingBot bot;

    public PingCommandStrategy(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @Override
    public void invoke(String messageText, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("pong");

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.PING;
    }
}
