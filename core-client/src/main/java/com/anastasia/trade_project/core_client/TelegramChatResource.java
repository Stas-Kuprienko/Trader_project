package com.anastasia.trade_project.core_client;

import com.anastasia.trade_project.dto.TelegramChatDto;
import org.springframework.web.client.RestClient;
import java.util.Optional;

public class TelegramChatResource extends HttpError404Handler {

    private static final String resourceUrl = "/telegram-chats";

    private final RestClient restClient;


    TelegramChatResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public Optional<TelegramChatDto> findById(long chatId) {
        return process(() -> restClient.get()
                .uri(CoreServiceClientV1.uriById(chatId))
                .retrieve()
                .body(TelegramChatDto.class));
    }
}
