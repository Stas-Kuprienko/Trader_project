package com.anastasia.telegram_bot.domain.session;

import com.anastasia.telegram_bot.datasource.ChatSessionRepository;
import com.anastasia.telegram_bot.exception.UnregisteredUserException;
import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatSessionService {

    private final ChatSessionRepository repository;
    private final CoreServiceClientV1 coreServiceClient;

    @Autowired
    public ChatSessionService(ChatSessionRepository repository, CoreServiceClientV1 coreServiceClient) {
        this.repository = repository;
        this.coreServiceClient = coreServiceClient;
    }


    public Mono<ChatSession> create(long chatId) {
        return Mono.just(ChatSession.createNew(chatId))
                .doOnNext(chatSession -> repository.save(chatSession).subscribe());
    }

    public Mono<Boolean> save(ChatSession chatSession) {
        return repository.save(chatSession);
    }

    public Mono<ChatSession> get(long chatId) {
        return repository
                .find(chatId)
                .switchIfEmpty(Mono.just(coreServiceClient.TELEGRAM_CHAT
                            .findById(chatId)
                            .orElseThrow(() -> new UnregisteredUserException(chatId)))
                            .flatMap(chat -> create(chatId)));
    }
}
