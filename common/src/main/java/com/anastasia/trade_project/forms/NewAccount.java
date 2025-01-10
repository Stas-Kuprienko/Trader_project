package com.anastasia.trade_project.forms;

import com.anastasia.trade_project.enums.Broker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class NewAccount {

    @NotNull
    private UUID userId;

    @NotNull
    @JsonProperty("client_id")
    private String clientId;

    @NotNull
    private Broker broker;

    @NotNull
    private String token;

    @JsonProperty("token_expires_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Schema(pattern = "YYYY-MM-DD")
    private String tokenExpiresAt;


    @Builder
    public NewAccount(UUID userId,
                      String clientId,
                      Broker broker,
                      String token,
                      String tokenExpiresAt) {
        this.userId = userId;
        this.clientId = clientId;
        this.broker = broker;
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;

    }

    public NewAccount() {
    }
}
