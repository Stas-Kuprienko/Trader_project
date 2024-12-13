package com.anastasia.telegram_bot.utils;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MonoVoidWrapper {

    Mono<? extends BotApiMethodMessage> perform();
}
