package com.sabinaber.berezinabot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;


@SpringBootApplication
public class BerezinaBotApplication {

	private final Dotenv dotenv;

	public BerezinaBotApplication(Dotenv dotenv) {
		this.dotenv = dotenv;
	}

	@PostConstruct
	public void init() {
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}

	public static void main(String[] args) {
		SpringApplication.run(BerezinaBotApplication.class, args);
	}
}

