package com.project.telegram.datasource;

import com.project.telegram.domain.session.ChatSession;
import reactor.core.publisher.Mono;

public interface ChatSessionRepository {

    Mono<Boolean> save(ChatSession chatSession);

    Mono<ChatSession> find(long chatId);
}
