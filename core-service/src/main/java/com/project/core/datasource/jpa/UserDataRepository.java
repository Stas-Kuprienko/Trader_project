package com.project.core.datasource.jpa;

import com.project.core.entity.User;
import com.project.enums.Role;
import com.project.enums.Status;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserDataRepository extends ReactiveCrudRepository<User, UUID> {


    @Modifying
    @Transactional
    @Query("""
            UPDATE person.users
            SET login = :login,
            updated_at = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateLogin(@Param("id") @NonNull UUID id,
                           @Param("login") @NonNull String login,
                           @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE person.users
            SET role = :role,
            updated_at = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateRole(@Param("id") @NonNull UUID id,
                          @Param("role") @NonNull Role role,
                          @Param("updatedAt") @NonNull LocalDateTime updatedAt);


    @Modifying
    @Transactional
    @Query("""
            UPDATE person.users
            SET status = :status,
            updated_at = :updatedAt
            WHERE id = :id""")
    Mono<Void> updateStatus(@Param("id") @NonNull UUID id,
                            @Param("status") @NonNull Status status,
                            @Param("updatedAt") @NonNull LocalDateTime updatedAt);
}
