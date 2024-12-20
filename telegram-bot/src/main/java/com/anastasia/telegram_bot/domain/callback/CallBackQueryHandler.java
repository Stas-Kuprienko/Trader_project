package com.anastasia.telegram_bot.domain.callback;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import reactor.core.publisher.Mono;

@Component
public class CallBackQueryHandler {


    public Mono<BotApiMethodMessage> apply(CallbackQuery callbackQuery) {

        return Mono.empty();
    }
}
