package com.anastasia.core_service.entity.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Table("telegram_chat")
public class TelegramChat {

    @Id
    @Column("chat_id")
    private Long chatId;

    private User user;

    @Column("created_at")
    private LocalDate createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public TelegramChat(Long chatId,
                        User user,
                        LocalDate createdAt,
                        LocalDateTime updatedAt) {
        this.chatId = chatId;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
                ", user=" + user +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}