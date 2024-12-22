package com.anastasia.core_service.service;

import com.anastasia.core_service.entity.user.User;
import reactor.core.publisher.Mono;

public interface UserDataService {

    Mono<User> create(User user);

    Mono<User> getById(Long id);
}
