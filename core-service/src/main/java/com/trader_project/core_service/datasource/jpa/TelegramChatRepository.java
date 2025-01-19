package com.trader_project.core_service.datasource.jpa;

import com.trader_project.core_service.entity.TelegramChat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface TelegramChatRepository extends ReactiveCrudRepository<TelegramChat, Long> {


    @Query("""
            SELECT * FROM person.telegram_chat
            WHERE user_id = :userId
            """)
    Mono<TelegramChat> findByUserId(@Param("userId") @NonNull UUID userId);
}
