package com.anastasia.telegram_bot.datasource;

import com.anastasia.telegram_bot.domain.session.ChatSession;
import reactor.core.publisher.Mono;

public interface ChatSessionRepository {

    Mono<Void> put(ChatSession chatSession);

    Mono<ChatSession> get(long chatId);
}
