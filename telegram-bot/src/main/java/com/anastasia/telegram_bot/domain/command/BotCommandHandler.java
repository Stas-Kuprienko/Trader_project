package com.anastasia.telegram_bot.domain.command;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;

public interface BotCommandHandler {

    Mono<BotApiMethodMessage> handle(Message message);
}
