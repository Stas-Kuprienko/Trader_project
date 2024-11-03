package com.anastasia.core_service.entity.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.Objects;

@Data
@Table("telegram_chat")
public class TelegramChat {

    @Id
    private Long chatId;

    private User user;


    public TelegramChat(Long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
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
                '}';
    }
}