package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.user.Account;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, UUID> {


    @Override
    @NonNull
    @Query("""
            SELECT a FROM Account a
            LEFT JOIN FETCH a.user u
            LEFT JOIN FETCH a.riskProfile rp
            WHERE a.id = :id
            """)
    Mono<Account> findById(@Param("id") @NonNull UUID uuid);
}
