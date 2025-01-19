package com.trader_project.core_service.entity;

import com.trade_project.enums.Broker;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Table(name = "account", schema = "person")
public class Account {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("client_id")
    private String clientId;

    private Broker broker;

    private String token;

    @Column("token_expires_at")
    private LocalDate tokenExpiresAt;

    @Column("risk_profile_id")
    private UUID riskProfileId;

    @Column("created_at")
    private LocalDate createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public Account(UUID id,
                   UUID userId,
                   String clientId,
                   Broker broker,
                   String token,
                   LocalDate tokenExpiresAt,
                   UUID riskProfileId,
                   LocalDate createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.broker = broker;
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
        this.riskProfileId = riskProfileId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Account() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(clientId, account.clientId) &&
                broker == account.broker &&
                Objects.equals(createdAt, account.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, broker, createdAt);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", broker='" + broker + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}