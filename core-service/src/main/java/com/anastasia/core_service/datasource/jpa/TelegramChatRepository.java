package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.user.TelegramChat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatRepository extends ReactiveCrudRepository<TelegramChat, Long> {}
