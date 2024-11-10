package com.anastasia.telegram_bot.domain.session;

import com.anastasia.telegram_bot.domain.command.BotCommand;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import java.util.HashMap;
import java.util.Map;

@Data
@RedisHash("chat_session")
public class ChatSession {

    private Long chatId;
    private Context context;


    @Builder
    public ChatSession(Long chatId, Context context) {
        this.chatId = chatId;
        this.context = context;
    }

    public ChatSession() {}



    @Getter @Setter
    public static class Context {

        private BotCommand command;
        private Map<String, Object> attributes;

        public Context(BotCommand command, Map<String, Object> attributes) {
            this.command = command;
            this.attributes = attributes;
        }

        public Context() {}
    }
}
