package com.anastasia.telegram_bot.controller;

import com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler;
import com.anastasia.telegram_bot.exception.UnregisteredUserException;
import com.anastasia.telegram_bot.utils.MonoVoidWrapper;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class TelegramLongPollingBotWithExceptionHandling extends TelegramLongPollingBot {

    private final TelegramBotExceptionHandler exceptionHandler;


    protected TelegramLongPollingBotWithExceptionHandling(String botToken, TelegramBotExceptionHandler exceptionHandler) {
        super(botToken);
        this.exceptionHandler = exceptionHandler;
    }


    protected final void process(Update update, MonoVoidWrapper onUpdateReceived) {
        try {
            log.info(update.getMessage().toString());
            onUpdateReceived
                    .perform()
                    .subscribe(this::executeAsync);
        } catch (UnregisteredUserException e) {
            exceptionHandler
                    .unregisteredUserHandle(e, update)
                    .subscribe(this::executeAsync);
        } catch (NotFoundException e) {
            exceptionHandler
                    .notFoundHandle(e, update)
                    .subscribe(this::executeAsync);
        } catch (IllegalArgumentException e) {
            exceptionHandler
                    .illegalArgumentHandle(e, update)
                    .subscribe(this::executeAsync);
        } catch (Exception e) {
            exceptionHandler
                    .defaultHandle(e, update)
                    .subscribe(this::executeAsync);
        }
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) {
        try {
            return super.execute(method);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> executeAsync(Method method) {
        try {
            return super.executeAsync(method);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            //TODO
            throw new RuntimeException(e);
        }
    }
}
