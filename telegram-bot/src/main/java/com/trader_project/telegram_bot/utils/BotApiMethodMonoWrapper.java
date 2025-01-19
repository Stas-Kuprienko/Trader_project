package com.trader_project.telegram_bot.utils;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BotApiMethodMonoWrapper {

    Mono<BotApiMethod<?>> perform();
}
