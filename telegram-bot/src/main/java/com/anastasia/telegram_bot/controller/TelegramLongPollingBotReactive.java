package com.anastasia.telegram_bot.controller;

import com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler;
import com.anastasia.telegram_bot.utils.BotApiMethodMonoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class TelegramLongPollingBotReactive extends TelegramLongPollingBot {

    private final TelegramBotExceptionHandler exceptionHandler;


    protected TelegramLongPollingBotReactive(String botToken, TelegramBotExceptionHandler exceptionHandler) {
        super(botToken);
        this.exceptionHandler = exceptionHandler;
    }


    protected final void process(Update update, BotApiMethodMonoWrapper onUpdateReceived) {
        log.info(update.toString());
        onUpdateReceived
                .perform()
                .doOnError(throwable -> exceptionHandler
                        .apply(throwable, update)
                        .subscribe(this::executeAsync))
                .subscribe(this::executeAsync);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) {
        try {
            return super.execute(method);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> executeAsync(Method method) {
        try {
            return super.executeAsync(method);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            //TODO
            throw new RuntimeException(e);
        }
    }
}
