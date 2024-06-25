package com.sabinaber.berezinabot.infrastructure.config;

import com.sabinaber.berezinabot.adapters.handler.CommandHandler;
import com.sabinaber.berezinabot.adapters.strategy.CommandStrategy;
import com.sabinaber.berezinabot.adapters.strategy.impl.PingCommandStrategy;
import com.sabinaber.berezinabot.adapters.strategy.impl.UserRegistrationCommandStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class CommandConfig {

    @Bean
    public CommandHandler commandHandler(List<CommandStrategy> strategyList, WebClient webClient) {
        return new CommandHandler(strategyList, webClient);
    }

    @Bean
    public CommandStrategy pingCommandStrategy() {
        return new PingCommandStrategy();
    }

    @Bean
    public CommandStrategy userRegistrationCommandStrategy() {
        return new UserRegistrationCommandStrategy();
    }
}












