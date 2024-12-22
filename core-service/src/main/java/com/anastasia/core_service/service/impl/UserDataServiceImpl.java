package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.datasource.jpa.UserDataRepository;
import com.anastasia.core_service.entity.user.User;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }


    @Override
    public Mono<User> create(User user) {
        //TODO
        return userDataRepository.save(user);
    }

    @Override
    public Mono<User> getById(Long id) {
        return userDataRepository
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.byID(User.class, id)));
    }
}
