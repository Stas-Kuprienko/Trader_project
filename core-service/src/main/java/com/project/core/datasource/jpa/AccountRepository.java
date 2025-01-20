package com.project.core.datasource.jpa;

import com.project.core.entity.Account;
import com.project.enums.Broker;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, UUID> {


    @Query("""
            SELECT * FROM person.account
            WHERE broker = :broker
            AND client_id = :clientId
            """)
    Mono<Account> findByBrokerAndClientId(@Param("broker") @NonNull Broker broker,
                                          @Param("clientId") @NonNull String clientId);


    @Query("""
            SELECT * FROM person.account
            WHERE user_id = :userId
            """)
    Flux<Account> findAllByUserId(@Param("userId") @NonNull UUID userId);


    @Modifying
    @Transactional
    @Query("""
            UPDATE person.account
            SET token = :token,
            token_expires_at = :tokenExpiresAt,
            updated_at = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateToken(@Param("id") @NonNull UUID id,
                           @Param("token") @NonNull String token,
                           @Param("tokenExpiresAt") LocalDate tokenExpiresAt,
                           @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE person.account
            SET risk_profile_id = :riskProfileId,
            updated_at = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateRiskProfile(@Param("id") @NonNull UUID id,
                                 @Param("riskProfileId") @NonNull UUID riskProfileId,
                                 @Param("updatedAt") @NonNull LocalDateTime updatedAt);
}
