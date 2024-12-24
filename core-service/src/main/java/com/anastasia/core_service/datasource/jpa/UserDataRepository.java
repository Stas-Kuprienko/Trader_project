package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.user.User;
import com.anastasia.trade_project.enums.Language;
import com.anastasia.trade_project.enums.Role;
import com.anastasia.trade_project.enums.Status;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

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


    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET name = :name,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateName(@Param("id") @NonNull Long id,
                          @Param("name") @NonNull String name,
                          @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET language = :language,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateLanguage(@Param("id") @NonNull Long id,
                              @Param("language") @NonNull Language language,
                              @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET login = :login,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateLogin(@Param("id") @NonNull Long id,
                           @Param("login") @NonNull String login,
                           @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET role = :role,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateRole(@Param("id") @NonNull Long id,
                          @Param("role") @NonNull Role role,
                          @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET status = :status,
            updatedAt = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateStatus(@Param("id") @NonNull Long id,
                            @Param("status") @NonNull Status status,
                            @Param("updatedAt") @NonNull LocalDateTime updatedAt);
}
