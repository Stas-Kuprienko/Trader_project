package com.anastasia.notifications.service.impl;

import com.anastasia.notifications.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class RestTelegramService implements TelegramService {

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot%s/sendMessage";

    private final String botToken;


    @Autowired
    public RestTelegramService(@Value("${telegram.botToken}") String botToken) {
        this.botToken = botToken;
    }


    @Override
    public void sendMessage(String text, Long chatId) {
        String url = TELEGRAM_API_URL.formatted(botToken);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = Map.of(
                "chat_id", chatId,
                "text", text
        );

        restTemplate.postForObject(url, params, String.class);
    }
}
