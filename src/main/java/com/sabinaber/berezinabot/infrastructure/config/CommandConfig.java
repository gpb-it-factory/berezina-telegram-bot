package com.sabinaber.berezinabot.infrastructure.config;

import com.sabinaber.berezinabot.adapters.handler.CommandHandler;
import com.sabinaber.berezinabot.adapters.strategy.CommandStrategy;
import com.sabinaber.berezinabot.adapters.strategy.impl.PingCommandStrategy;
import com.sabinaber.berezinabot.adapters.strategy.impl.UserRegistrationCommandStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class CommandConfig {

    @Bean
    public CommandHandler commandHandler(List<CommandStrategy> strategyList, RestTemplate restTemplate) {
        return new CommandHandler(strategyList, restTemplate);
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











