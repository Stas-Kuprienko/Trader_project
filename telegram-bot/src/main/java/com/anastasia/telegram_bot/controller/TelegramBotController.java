package com.anastasia.telegram_bot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotController extends TelegramLongPollingBot {

    private final String username;


    @Autowired
    public TelegramBotController(@Value("${telegrambots.bot.username}") String username,
                                 @Value("${telegrambots.bot.token}") String botToken) {
        super(botToken);
        this.username = username;
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            System.out.println(update.getMessage());
        }
    }


    private void setMenuIfStart(Update update) throws TelegramApiException {
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equals("/start")) {
                //TODO
                executeAsync(new SetMyCommands());
            }
            System.out.println(update.getMessage().getText());
        }
    }
}