package com.trader_project.core_service.service;

import com.trader_project.core_service.entity.TelegramChat;
import com.trader_project.core_service.entity.User;
import com.trade_project.enums.Language;
import com.trade_project.enums.Status;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserDataService {

    Mono<User> singUp(String login, String password, String name, Language language);

    Mono<TelegramChat> createTelegramChat(TelegramChat telegramChat);

    Mono<User> getById(UUID id);

    Mono<TelegramChat> getTelegramChatById(Long chatId);

    Mono<TelegramChat> getTelegramChatByUser(UUID userId);

    Mono<User> update(UUID id, User updatable);

    Mono<Void> setStatus(UUID id, Status status);

    Mono<Void> delete(UUID id);
}
