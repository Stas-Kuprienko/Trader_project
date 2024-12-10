package com.anastasia.telegram_bot.domain.session;

import com.anastasia.telegram_bot.datasource.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatSessionService {

    private final ChatSessionRepository repository;

    @Autowired
    public ChatSessionService(ChatSessionRepository repository) {
        this.repository = repository;
    }


    public Mono<ChatSession> create(long chatId) {
        return Mono.just(ChatSession.createNew(chatId))
                .doOnNext(chatSession -> repository.put(chatSession).subscribe());
    }

    public Mono<Void> save(ChatSession chatSession) {
        return repository.put(chatSession);
    }

    public Mono<ChatSession> get(long chatId) {
        return repository.get(chatId);
    }
}
