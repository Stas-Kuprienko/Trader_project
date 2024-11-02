package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class AccountDto {

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("client_id")
    private String clientId;

    private String broker;

    private String token;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3})$")
    @JsonProperty("token_expires_at")
    private String tokenExpiresAt;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    @JsonProperty("risk_profile_id")
    private String riskProfileId;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3})$")
    @JsonProperty("created_at")
    private String createdAt;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3}) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$")
    @JsonProperty("updated_at")
    private String updatedAt;


    @Builder
    public AccountDto(String id,
                      Long userId,
                      String clientId,
                      String broker,
                      String token,
                      String tokenExpiresAt,
                      String riskProfileId,
                      String createdAt,
                      String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.broker = broker;
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
        this.riskProfileId = riskProfileId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AccountDto() {}
}
