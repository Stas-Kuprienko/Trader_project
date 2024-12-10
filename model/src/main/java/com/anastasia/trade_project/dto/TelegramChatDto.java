package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class TelegramChatDto {

    @JsonProperty("chat_id")
    private Long chatId;

    private UserDto user;


    @Builder
    public TelegramChatDto(Long chatId,
                        UserDto user) {
        this.chatId = chatId;
        this.user = user;
    }

    public TelegramChatDto() {}
}
