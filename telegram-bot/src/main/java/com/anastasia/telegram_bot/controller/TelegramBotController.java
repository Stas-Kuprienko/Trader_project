package com.anastasia.telegram_bot.controller;

import com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler;
import com.anastasia.telegram_bot.domain.command.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class TelegramBotController extends TelegramLongPollingBotWithExceptionHandling {

    private final CommandDispatcher commandDispatcher;
    private final String username;


    @Autowired
    public TelegramBotController(TelegramBotExceptionHandler controllerAdvice,
                                 CommandDispatcher commandDispatcher,
                                 @Value("${telegram.username}") String username,
                                 @Value("${telegram.botToken}") String botToken) {
        super(botToken, controllerAdvice);
        this.username = username;
        this.commandDispatcher = commandDispatcher;
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        process(update, () -> {
            if (update.hasMessage()) {
                log.info(update.getMessage().toString());
                commandDispatcher
                        .apply(update.getMessage())
                        .subscribe(this::executeAsync);
            }
        });
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