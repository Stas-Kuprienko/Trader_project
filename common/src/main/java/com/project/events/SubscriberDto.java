package com.project.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class SubscriberDto implements Serializable {

    @JsonIgnore
    public static final Operation SAVE = Operation.SAVE;

    @JsonIgnore
    public static final Operation DELETE = Operation.DELETE;


    private Operation operation;

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
    public SubscriberDto(Operation operation,
                         UUID accountId,
                         String email,
                         boolean emailNotify,
                         Long telegramId,
                         boolean telegramNotify,
                         Language language,
                         String name,
                         String createdAt,
                         String updatedAt) {
        this.operation = operation;
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


    public enum Operation {
        SAVE, DELETE
    }
}
