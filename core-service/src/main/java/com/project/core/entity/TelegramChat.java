package com.project.core.entity;

import com.project.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Table(name = "telegram_chat", schema = "person")
public class TelegramChat {

    @Id
    @Column("chat_id")
    private Long chatId;

    @Column("user_id")
    private UUID userId;

    private Status status;

    @Column("created_at")
    private LocalDate createdAt;


    @Builder
    public TelegramChat(Long chatId,
                        UUID userId,
                        Status status,
                        LocalDate createdAt) {
        this.chatId = chatId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public TelegramChat() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TelegramChat that)) return false;
        return Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return "TelegramChat{" +
                "chatId=" + chatId +
                ", userId=" + userId +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}