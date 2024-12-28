package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.datasource.jpa.TelegramChatRepository;
import com.anastasia.core_service.datasource.jpa.UserDataRepository;
import com.anastasia.core_service.entity.TelegramChat;
import com.anastasia.core_service.entity.User;
import com.anastasia.core_service.exception.DataPersistenceException;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.service.UserDataService;
import com.anastasia.trade_project.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;
    private final TelegramChatRepository telegramChatRepository;

    @Autowired
    public UserDataServiceImpl(UserDataRepository userDataRepository, TelegramChatRepository telegramChatRepository) {
        this.userDataRepository = userDataRepository;
        this.telegramChatRepository = telegramChatRepository;
    }


    @Override
    public Mono<User> create(User user) {
        if (user.getId() != null) {
            throw DataPersistenceException.entityToSaveHasId(user);
        }
        LocalDate createdAt = LocalDate.now();
        user.setCreatedAt(createdAt);
        return userDataRepository.save(user);
    }

    @Override
    public Mono<TelegramChat> createTelegramChat(TelegramChat telegramChat) {
        LocalDate createdAt = LocalDate.now();
        telegramChat.setCreatedAt(createdAt);
        return telegramChatRepository.save(telegramChat);
    }

    @Override
    public Mono<User> getById(UUID id) {
        return userDataRepository
                .findById(id)
                .filter(user -> user.getStatus() == Status.ACTIVE)
                .switchIfEmpty(Mono.error(NotFoundException.byID(User.class, id)));
    }

    @Override
    public Mono<TelegramChat> getTelegramChatById(Long chatId) {
        return telegramChatRepository
                .findById(chatId)
                .filter(telegramChat -> telegramChat.getStatus() == Status.ACTIVE)
                .switchIfEmpty(Mono.error(NotFoundException.byID(TelegramChat.class, chatId)));
    }

    @Override
    public Mono<TelegramChat> getTelegramChatByUser(User user) {
        return telegramChatRepository
                .findByUser(user)
                .filter(telegramChat -> telegramChat.getStatus() == Status.ACTIVE)
                .switchIfEmpty(Mono.error(
                        NotFoundException.byParameter(TelegramChat.class, "userID", user.getId())));
    }

    @Override
    public Mono<User> update(UUID id, User updatable) {
        return userDataRepository
                .findById(id)
                .flatMap(user -> {
                    setIfNotNull(updatable::getName, user::setName);
                    setIfNotNull(updatable::getLanguage, user::setLanguage);
                    user.setUpdatedAt(dateTimeNow());
                    return userDataRepository.save(user);
                });
    }

    @Override
    public Mono<Void> setStatus(UUID id, Status status) {
        return userDataRepository
                .updateStatus(id, status, dateTimeNow());
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return userDataRepository
                .deleteById(id);
    }


    private LocalDateTime dateTimeNow() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    private <T> void setIfNotNull(Supplier<T> supplier, Consumer<T> consumer) {
        if (supplier.get() != null) {
            consumer.accept(supplier.get());
        }
    }
}
