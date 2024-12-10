package com.anastasia.telegram_bot.domain.session;

import com.anastasia.telegram_bot.domain.command.BotCommand;
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

    public ChatSession(Long chatId, Context context) {
        this.chatId = chatId;
        this.context = context;
    }

    public ChatSession() {}


    public static ChatSession createNew(long chatId) {
        return new ChatSession(chatId, new Context(BotCommand.RESET, new HashMap<>(), 0));
    }


    @Getter @Setter
    public static class Context {

        private BotCommand command;
        private Map<String, Object> attributes;
        private int step;

        public Context(BotCommand command, Map<String, Object> attributes, int step) {
            this.command = command;
            this.attributes = attributes;
            this.step = step;
        }

        public Context() {}
    }
}
