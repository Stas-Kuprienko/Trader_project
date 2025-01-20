package com.project.core.service.converter;

import com.project.core.entity.TelegramChat;
import com.project.dto.TelegramChatDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TelegramChatConverter implements Converter<TelegramChat, TelegramChatDto> {


    @Override
    public Mono<TelegramChatDto> toDto(TelegramChat telegramChat) {
        return Mono.just(
                TelegramChatDto.builder()
                        .chatId(telegramChat.getChatId())
                        .userId(telegramChat.getUserId())
                        .status(telegramChat.getStatus())
                        .createdAt(localDateToString(telegramChat.getCreatedAt()))
                        .build()
        );
    }

    @Override
    public Mono<TelegramChat> toEntity(TelegramChatDto dto) {
        return Mono.just(
                TelegramChat.builder()
                        .chatId(dto.getChatId())
                        .userId(dto.getUserId())
                        .status(dto.getStatus())
                        .createdAt(stringToLocalDate(dto.getCreatedAt()))
                        .build()
        );
    }
}
