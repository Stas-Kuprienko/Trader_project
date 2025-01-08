package com.anastasia.notifications.entity;

import com.anastasia.trade_project.enums.Language;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "subscriber", schema = "notification")
@Getter @Setter
public class Subscriber {

    @Id
    @Column(name = "account_id")
    private UUID accountId;

    private String email;

    @Column(name = "email_notify")
    private boolean emailNotify;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "telegram_notify")
    private boolean telegramNotify;

    @Enumerated(value = EnumType.STRING)
    private Language language;

    private String name;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public Subscriber(UUID accountId,
                      String email,
                      boolean emailNotify,
                      Long telegramId,
                      boolean telegramNotify,
                      Language language,
                      String name,
                      LocalDate createdAt,
                      LocalDateTime updatedAt) {
        this.accountId = accountId;
        this.email = email;
        this.emailNotify = emailNotify;
        this.telegramId = telegramId;
        this.telegramNotify = telegramNotify;
        this.language = language;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Subscriber() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscriber that)) return false;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(email, that.email) &&
                Objects.equals(telegramId, that.telegramId) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, email, telegramId, createdAt);
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", emailNotify=" + emailNotify +
                ", telegramId=" + telegramId +
                ", telegramNotify=" + telegramNotify +
                ", language=" + language +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
