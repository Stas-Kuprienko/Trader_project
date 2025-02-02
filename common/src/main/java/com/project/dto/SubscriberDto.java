package com.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class SubscriberDto {

    @JsonProperty("account_id")
    private UUID accountId;

    private String email;

    @JsonProperty("email_notify")
    private boolean emailNotify;

    @JsonProperty("telegram_id")
    private Long telegramId;

    @JsonProperty("telegram_notify")
    private boolean telegramNotify;

    private Language language;

    private String name;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Schema(pattern = "YYYY-MM-DD")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    @Schema(pattern = "YYYY-MM-DD HH:MM:SS")
    private String updatedAt;


    @Builder
    public SubscriberDto(UUID accountId,
                         String email,
                         boolean emailNotify,
                         Long telegramId,
                         boolean telegramNotify,
                         Language language,
                         String name,
                         String createdAt,
                         String updatedAt) {
        this.accountId = accountId;
        this.email = email;
        this.emailNotify = emailNotify;
        this.telegramId = telegramId;
        this.telegramNotify = telegramNotify;
        this.language = language;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public SubscriberDto() {}
}
