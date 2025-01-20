package com.project.telegram.datasource.redis;

import com.project.telegram.datasource.ChatSessionRepository;
import com.project.telegram.domain.session.ChatSession;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Repository
public class ChatSessionRepositoryRedis implements ChatSessionRepository {

    private static final String CHAT_SESSION_KEY = "CHAT_SESSION";

    private final ReactiveRedisTemplate<String, ChatSession> redisTemplate;


    @Autowired
    public ChatSessionRepositoryRedis(@Qualifier("chatSessionRedisTemplate") ReactiveRedisTemplate<String, ChatSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostConstruct
    public void init() {
        Duration duration = Duration.of(5, ChronoUnit.MINUTES);
        redisTemplate.expire(CHAT_SESSION_KEY, duration);
        log.info("Cache duration for {} key is set to {}", CHAT_SESSION_KEY, duration);
    }

    @Override
    public Mono<Boolean> save(ChatSession chatSession) {
        return redisTemplate
                .opsForHash()
                .put(CHAT_SESSION_KEY, chatSession.getChatId().toString(), chatSession)
                .doOnNext(b -> log.debug("The chat session for ID {} is cached", chatSession.getChatId()));
    }

    @Override
    public Mono<ChatSession> find(long chatId) {
        return redisTemplate.opsForHash()
                .get(CHAT_SESSION_KEY, String.valueOf(chatId))
                .map(ChatSession.class::cast)
                .doOnNext(session -> log.debug("The chat session for ID {} is retrieved", chatId));
    }
}
