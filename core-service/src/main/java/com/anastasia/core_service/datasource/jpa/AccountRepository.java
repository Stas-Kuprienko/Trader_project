package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.Account;
import com.anastasia.core_service.entity.RiskProfile;
import com.anastasia.trade_project.enums.Broker;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    Mono<Account> findById(@Param("id") @NonNull UUID id);


    @Query("""
            SELECT a FROM Account a
            LEFT JOIN FETCH a.user u
            LEFT JOIN FETCH a.riskProfile rp
            WHERE a.broker = :broker AND
            a.clientId = :clientId
            """)
    Mono<Account> findByBrokerAndClientId(@Param("broker") @NonNull Broker broker,
                                          @Param("clientId") @NonNull String clientId);


    @Modifying
    @Transactional
    @Query("""
            UPDATE Account
            SET token = :token,
            tokenExpiresAt = :tokenExpiresAt,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateToken(@Param("id") @NonNull UUID id,
                           @Param("token") @NonNull String token,
                           @Param("tokenExpiresAt") LocalDate tokenExpiresAt,
                           @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE Account
            SET riskProfile = :riskProfile,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateRiskProfile(@Param("id") @NonNull UUID id,
                                 @Param("riskProfile") @NonNull RiskProfile riskProfile,
                                 @Param("updatedAt") @NonNull LocalDateTime updatedAt);
}
