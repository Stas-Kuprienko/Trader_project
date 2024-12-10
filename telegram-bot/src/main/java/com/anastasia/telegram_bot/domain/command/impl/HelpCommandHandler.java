package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommand;
import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;

@CommandHandler(command = BotCommand.HELP)
public class HelpCommandHandler implements BotCommandHandler {

    @Override
    public Mono<BotApiMethodMessage> handle(Message message) {
        return null;
    }
}
