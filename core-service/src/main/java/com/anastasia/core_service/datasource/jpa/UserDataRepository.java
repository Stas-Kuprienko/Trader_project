package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserDataRepository extends ReactiveCrudRepository<User, Long> {


    @Override
    @NonNull
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.accounts a
            WHERE u.id = :id
            """)
    Mono<User> findById(@Param("id") @NonNull Long id);
}
