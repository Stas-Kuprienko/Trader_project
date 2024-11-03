package com.anastasia.core_service.entity.user;

import com.anastasia.core_service.entity.enums.Broker;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Data
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


    @Builder
    public Account(UUID id,
                   User user,
                   String clientId,
                   Broker broker,
                   String token,
                   LocalDate tokenExpiresAt,
                   RiskProfile riskProfile) {
        this.id = id;
        this.user = user;
        this.clientId = clientId;
        this.broker = broker;
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
        this.riskProfile = riskProfile;
    }

    public Account() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(user, account.user) &&
                Objects.equals(clientId, account.clientId) &&
                Objects.equals(broker, account.broker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, user, broker);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + user.getLogin() +
                ", clientId='" + clientId + '\'' +
                ", broker='" + broker + '\'' +
                ", riskProfile='" + riskProfile + '\'' +
                '}';
    }
}