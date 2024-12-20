package com.anastasia.telegram_bot.domain.session;

import com.anastasia.telegram_bot.domain.command.BotCommands;
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
        return new ChatSession(chatId, new Context(null, new HashMap<>(), 0));
    }

    public Map<String, String> getAttributes() {
        return context.attributes;
    }

    public void clear() {
        context.setCommand(null);
        context.setStep(0);
        context.attributes.clear();
    }


    @Getter @Setter
    public static class Context {

        private BotCommands command;
        private Map<String, String> attributes;
        private int step;

        public Context(BotCommands command, Map<String, String> attributes, int step) {
            this.command = command;
            this.attributes = attributes;
            this.step = step;
        }

        public Context() {}
    }
}
