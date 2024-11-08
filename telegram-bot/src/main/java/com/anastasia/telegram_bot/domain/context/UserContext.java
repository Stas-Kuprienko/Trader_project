package com.anastasia.telegram_bot.domain.context;

import com.anastasia.telegram_bot.domain.command.Commands;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import java.util.HashMap;
import java.util.Map;

@Data
@RedisHash("user_context")
public class UserContext {

    private Long chatId;
    private Commands last;
    private final Map<String, Object> data;


    @Builder
    public UserContext(Long chatId, Commands last, Map<String, Object> data) {
        this.chatId = chatId;
        this.last = last;
        this.data = data;
    }

    public UserContext() {
        data = new HashMap<>();
    }
}
