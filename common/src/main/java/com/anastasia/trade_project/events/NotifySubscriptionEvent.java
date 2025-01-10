package com.anastasia.trade_project.events;

import com.anastasia.trade_project.enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.util.UUID;

@Data
public class NotifySubscriptionEvent implements Serializable {

    @JsonIgnore
    public static final Notification EMAIL = Notification.EMAIL;

    @JsonIgnore
    public static final Notification TELEGRAM = Notification.TELEGRAM;

    @JsonProperty("account_id")
    private UUID accountId;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @JsonProperty("telegram_id")
    private Long telegramId;

    private Language language;

    private String name;

    private Notification[] notifications;


    @Builder
    public NotifySubscriptionEvent(UUID accountId,
                                   String email,
                                   Long telegramId,
                                   Notification[] notifications) {
        this.accountId = accountId;
        this.email = email;
        this.telegramId = telegramId;
        this.notifications = notifications;
    }

    public NotifySubscriptionEvent() {}


    public enum Notification {
        EMAIL, TELEGRAM
    }
}
