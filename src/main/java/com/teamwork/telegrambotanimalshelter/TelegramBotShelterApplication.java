package com.teamwork.telegrambotanimalshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBotShelterApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramBotShelterApplication.class);
    }
}
