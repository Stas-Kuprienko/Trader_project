package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.datasource.jpa.TelegramChatRepository;
import com.anastasia.core_service.datasource.jpa.UserDataRepository;
import com.anastasia.core_service.entity.user.TelegramChat;
import com.anastasia.core_service.entity.user.User;
import com.anastasia.core_service.exception.DataPersistenceException;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

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
    public Mono<User> getById(Long id) {
        return userDataRepository
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.byID(User.class, id)));
    }

    @Override
    public Mono<TelegramChat> getTelegramChatById(Long chatId) {
        return telegramChatRepository
                .findById(chatId)
                .switchIfEmpty(Mono.error(NotFoundException.byID(TelegramChat.class, chatId)));
    }

    @Override
    public Mono<TelegramChat> getTelegramChatByUser(User user) {
        return telegramChatRepository
                .findByUser(user)
                .switchIfEmpty(Mono.error(
                        NotFoundException.byParameter(TelegramChat.class, "userID", user.getId())));
    }
}
