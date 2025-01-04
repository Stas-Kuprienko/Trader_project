package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.TelegramChat;
import com.anastasia.trade_project.dto.TelegramChatDto;
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
