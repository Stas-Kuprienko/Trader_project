package com.anastasia.telegram_bot.datasource;

import com.anastasia.telegram_bot.domain.session.ChatSession;
import reactor.core.publisher.Mono;

public interface ChatSessionRepository {

    Mono<Boolean> save(ChatSession chatSession);

    Mono<ChatSession> find(long chatId);
}
