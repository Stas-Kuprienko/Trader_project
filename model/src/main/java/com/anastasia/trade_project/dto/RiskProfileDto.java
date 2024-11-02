package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class RiskProfileDto {

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String id;

    @JsonProperty("deal_loss_percentage")
    private byte dealLossPercentage;

    @JsonProperty("account_loss_percentage")
    private byte accountLossPercentage;

    @JsonProperty("futures_in_account_percentage")
    private byte futuresInAccountPercentage;

    @JsonProperty("stocks_in_account_percentage")
    private byte StockInAccountPercentage;

    @JsonProperty("risk_type")
    private String riskType;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3})$")
    @JsonProperty("created_at")
    private String createdAt;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3}) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$")
    @JsonProperty("updated_at")
    private String updatedAt;


    @Builder
    public RiskProfileDto(String id,
                          byte dealLossPercentage,
                          byte accountLossPercentage,
                          byte futuresInAccountPercentage,
                          byte stockInAccountPercentage,
                          String riskType,
                          String createdAt,
                          String updatedAt) {
        this.id = id;
        this.dealLossPercentage = dealLossPercentage;
        this.accountLossPercentage = accountLossPercentage;
        this.futuresInAccountPercentage = futuresInAccountPercentage;
        StockInAccountPercentage = stockInAccountPercentage;
        this.riskType = riskType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public RiskProfileDto() {}
}
