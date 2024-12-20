package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommands;
import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.session.ChatSession;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.Locale;

@CommandHandler(command = BotCommands.START)
public class StartCommandHandler implements BotCommandHandler {

    @Override
    public Mono<BotApiMethodMessage> handle(Message message, ChatSession session) {
        return null;
    }

    @Override
    public Mono<? extends BotApiMethodMessage> handle(String text, ChatSession session, Locale locale) {
        return null;
    }
}
