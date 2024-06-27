package com.sabinaber.berezinabot.infrastructure.config;

import com.sabinaber.berezinabot.adapters.handler.CommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CommandConfig {

    @Bean
    public CommandHandler commandHandler(WebClient webClient) {
        return new CommandHandler(webClient);
    }
}












