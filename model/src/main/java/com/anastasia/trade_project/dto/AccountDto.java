package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Broker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AccountDto {

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("client_id")
    private String clientId;

    private Broker broker;

    private String token;

    @JsonProperty("token_expires_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tokenExpiresAt;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    @JsonProperty("risk_profile_id")
    private String riskProfileId;


    @Builder
    public AccountDto(String id,
                      Long userId,
                      String clientId,
                      Broker broker,
                      String token,
                      LocalDate tokenExpiresAt,
                      String riskProfileId) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.broker = broker;
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
        this.riskProfileId = riskProfileId;
    }

    public AccountDto() {}
}
