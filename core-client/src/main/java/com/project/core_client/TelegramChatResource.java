package com.project.core_client;

import com.project.dto.TelegramChatDto;
import com.project.enums.Status;
import org.springframework.web.client.RestClient;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class TelegramChatResource extends HttpError404Handler {

    private static final String resourceUrl = "/telegram-chats";

    private final RestClient restClient;


    TelegramChatResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


//    public Optional<TelegramChatDto> findById(long chatId) {
//        return process(() -> restClient.get()
//                .uri(CoreServiceClientV1.uriById(chatId))
//                .retrieve()
//                .body(TelegramChatDto.class));
//    }

    //TODO TEMPORARY SOLUTION !!!!!!!!!!!!!!!!!!!!!!!

    public Optional<TelegramChatDto> findById(long chatId) {
        return Optional.of(new TelegramChatDto(chatId, UUID.randomUUID(), Status.ACTIVE, LocalDate.now().toString()));
    }
}
