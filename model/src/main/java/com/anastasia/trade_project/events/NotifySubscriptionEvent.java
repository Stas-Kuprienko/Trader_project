package com.anastasia.trade_project.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class NotifySubscriptionEvent {

    @JsonIgnore
    public static final Notification EMAIL = Notification.EMAIL;

    @JsonIgnore
    public static final Notification TELEGRAM = Notification.TELEGRAM;


    @NotNull
    @JsonProperty("user_id")
    private UUID userId;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    @Schema(pattern = "example@email\\.com")
    private String email;

    @JsonProperty("telegram_id")
    private Long telegramId;

    @NotNull
    private Notification[] notifications;


    @Builder
    public NotifySubscriptionEvent(UUID userId,
                                   String email,
                                   Long telegramId,
                                   Notification[] notifications) {
        this.userId = userId;
        this.email = email;
        this.telegramId = telegramId;
        this.notifications = notifications;
    }

    public NotifySubscriptionEvent() {}


    public enum Notification {
        EMAIL, TELEGRAM
    }
}
