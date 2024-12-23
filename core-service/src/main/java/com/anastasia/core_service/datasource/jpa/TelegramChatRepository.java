package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.user.TelegramChat;
import com.anastasia.core_service.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TelegramChatRepository extends ReactiveCrudRepository<TelegramChat, Long> {


    @Override
    @NonNull
    @Query("""
            SELECT t FROM TelegramChat t
            JOIN FETCH t.user u
            WHERE t.chatId = :chatId
            """)
    Mono<TelegramChat> findById(@Param("chatId") @NonNull Long chatId);


    @Query("""
            SELECT t FROM TelegramChat t
            JOIN FETCH t.user u
            WHERE t.user = :user
            """)
    Mono<TelegramChat> findByUser(@Param("user") @NonNull User user);
}
