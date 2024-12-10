package com.anastasia.telegram_bot.controller;

import org.springframework.context.MessageSource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.function.Consumer;

public abstract class TelegramLongPollingBotWithExceptionHandling extends TelegramLongPollingBot {

    private final MessageSource messageSource;

    public TelegramLongPollingBotWithExceptionHandling(String botToken, MessageSource messageSource) {
        super(botToken);
        this.messageSource = messageSource;
    }

    protected final void handleExceptions(Consumer<?> consumer) {
        try {
            consumer.accept(null);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO handling
            SendMessage sendMessage = new SendMessage();

            try {
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
