package com.anastasia.telegram_bot.controller;

import com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler;
import com.anastasia.telegram_bot.exception.UnregisteredUserException;
import com.anastasia.trade_project.util.JustAction;
import jakarta.ws.rs.NotFoundException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class TelegramLongPollingBotWithExceptionHandling extends TelegramLongPollingBot {

    private final TelegramBotExceptionHandler exceptionHandler;


    protected TelegramLongPollingBotWithExceptionHandling(String botToken, TelegramBotExceptionHandler exceptionHandler) {
        super(botToken);
        this.exceptionHandler = exceptionHandler;
    }


    protected final void process(Update update, JustAction onUpdateReceived) {
        try {
            onUpdateReceived.perform();
        } catch (UnregisteredUserException e) {
            SendMessage sendMessage = exceptionHandler.unregisteredUserHandle(e, update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
        } catch (NotFoundException e) {
            SendMessage sendMessage = exceptionHandler.notFoundHandle(e, update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            SendMessage sendMessage = exceptionHandler.defaultHandle(e, update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
