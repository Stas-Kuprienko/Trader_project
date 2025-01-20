package com.project.dto;

import com.project.enums.Broker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("client_id")
    private String clientId;

    private Broker broker;

    @JsonProperty("token_expires_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Schema(pattern = "YYYY-MM-DD")
    private String tokenExpiresAt;

    @JsonProperty("risk_profile_id")
    private UUID riskProfileId;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Schema(pattern = "YYYY-MM-DD")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    @Schema(pattern = "YYYY-MM-DD HH:MM:SS")
    private String updatedAt;


    @Builder
    public AccountDto(UUID id,
                      UUID userId,
                      String clientId,
                      Broker broker,
                      String tokenExpiresAt,
                      UUID riskProfileId,
                      String createdAt,
                      String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.broker = broker;
        this.tokenExpiresAt = tokenExpiresAt;
        this.riskProfileId = riskProfileId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AccountDto() {}
}
