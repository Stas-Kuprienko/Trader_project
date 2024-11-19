package com.anastasia.core_service.entity.user;

import com.anastasia.trade_project.enums.Broker;
import lombok.Builder;
import lombok.Data;
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
@Table("account")
public class Account {

    @Id
    private UUID id;

    private User user;

    @Column("client_id")
    private String clientId;

    private Broker broker;

    private String token;

    @Column("token_expires_at")
    private LocalDate tokenExpiresAt;

    @Column("risk_profile_id")
    private RiskProfile riskProfile;

    @Column("created_at")
    private LocalDate createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public Account(UUID id,
                   User user,
                   String clientId,
                   Broker broker,
                   String token,
                   LocalDate tokenExpiresAt,
                   RiskProfile riskProfile,
                   LocalDate createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.clientId = clientId;
        this.broker = broker;
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
        this.riskProfile = riskProfile;
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