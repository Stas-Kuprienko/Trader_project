package com.anastasia.core_service.entity;

import com.anastasia.trade_project.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.util.Objects;

@Getter @Setter
@Table(name = "telegram_chat", schema = "person")
public class TelegramChat {

    @Id
    @Column("chat_id")
    private Long chatId;

    private User user;

    private Status status;

    @Column("created_at")
    private LocalDate createdAt;


    @Builder
    public TelegramChat(Long chatId,
                        User user,
                        Status status,
                        LocalDate createdAt) {
        this.chatId = chatId;
        this.user = user;
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
                ", user=" + user +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}