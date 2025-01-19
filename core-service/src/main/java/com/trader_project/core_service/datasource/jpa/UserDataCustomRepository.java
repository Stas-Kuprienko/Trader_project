package com.trader_project.core_service.datasource.jpa;

import com.trader_project.core_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserDataCustomRepository {

    private final R2dbcEntityTemplate entityTemplate;

    @Autowired
    public UserDataCustomRepository(R2dbcEntityTemplate entityTemplate) {
        this.entityTemplate = entityTemplate;
    }


    public Mono<User> insert(User user) {
        return entityTemplate.insert(User.class).using(user);
    }
}
