package com.sabinaber.berezinabot.infrastructure.config;

import com.sabinaber.berezinabot.adapters.bot.TelegramBot;
import com.sabinaber.berezinabot.adapters.handler.CommandHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    private final Dotenv dotenv;

    public BotConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        return new DefaultBotOptions();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramBot);
        return telegramBotsApi;
    }

    @Bean
    public TelegramBot telegramBot(DefaultBotOptions options, CommandHandler commandHandler) {
        String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
        String botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
        return new TelegramBot(botToken, botUsername, options, commandHandler);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}










