package com.anastasia.telegram_bot.controller;

import com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler;
import com.anastasia.telegram_bot.domain.command.CommandDispatcher;
import com.anastasia.telegram_bot.domain.element.CommandMenuList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBotController extends TelegramLongPollingBotReactive {

    private final CommandDispatcher commandDispatcher;
    private final String username;


    @Autowired
    public TelegramBotController(TelegramBotExceptionHandler controllerAdvice,
                                 CommandMenuList commandMenuList,
                                 CommandDispatcher commandDispatcher,
                                 @Value("${telegram.username}") String username,
                                 @Value("${telegram.botToken}") String botToken) {
        super(botToken, controllerAdvice);
        this.username = username;
        this.commandDispatcher = commandDispatcher;
        this.execute(commandMenuList.getDefaultMenu());
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            process(update, () -> commandDispatcher.apply(update.getMessage()));

        } else if (update.hasCallbackQuery()) {
            process(update, () -> commandDispatcher.apply(update.getCallbackQuery()));
        }
    }
}