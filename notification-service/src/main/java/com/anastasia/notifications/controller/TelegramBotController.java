package com.anastasia.notifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBotController extends TelegramLongPollingBot {

    private final String username;


    @Autowired
    public TelegramBotController(@Value("${telegram.username}") String username,
                                 @Value("${telegram.botToken}") String botToken) {
        super(botToken);
        this.username = username;
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}