package com.project.telegram.domain.command.impl;

import com.project.telegram.domain.command.BotCommandHandler;
import com.project.telegram.domain.command.BotCommands;
import com.project.telegram.domain.session.ChatSession;
import com.project.telegram.domain.command.CommandHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.Locale;

@CommandHandler(command = BotCommands.START)
public class StartCommandHandler extends BotCommandHandler {

    @Override
    public Mono<BotApiMethod<?>> handle(Message message, ChatSession session) {
        return null;
    }

    @Override
    public Mono<BotApiMethod<?>> handle(CallbackQuery callbackQuery, ChatSession session, Locale locale) {
        return null;
    }
}
