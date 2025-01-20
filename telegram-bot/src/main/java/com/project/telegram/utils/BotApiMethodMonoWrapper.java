package com.project.telegram.utils;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BotApiMethodMonoWrapper {

    Mono<BotApiMethod<?>> perform();
}
