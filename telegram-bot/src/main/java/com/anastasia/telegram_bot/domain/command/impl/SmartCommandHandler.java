package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.BotCommands;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.session.ChatSession;
import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.Locale;

@CommandHandler(command = BotCommands.SMART)
public class SmartCommandHandler extends BotCommandHandler {

    private final MessageSource messageSource;
    private final CoreServiceClientV1 coreServiceClient;

    @Autowired
    public SmartCommandHandler(MessageSource messageSource, CoreServiceClientV1 coreServiceClient) {
        this.messageSource = messageSource;
        this.coreServiceClient = coreServiceClient;
    }


    @Override
    public Mono<BotApiMethod<?>> handle(Message message, ChatSession session) {
        return null;
    }

    @Override
    public Mono<BotApiMethod<?>> handle(CallbackQuery callbackQuery, ChatSession session, Locale locale) {
        return null;
    }
}
