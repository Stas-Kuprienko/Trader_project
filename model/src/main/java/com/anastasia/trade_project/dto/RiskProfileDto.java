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


    @Builder
    public RiskProfileDto(String id,
                          byte dealLossPercentage,
                          byte accountLossPercentage,
                          byte futuresInAccountPercentage,
                          byte stockInAccountPercentage,
                          String riskType) {
        this.id = id;
        this.dealLossPercentage = dealLossPercentage;
        this.accountLossPercentage = accountLossPercentage;
        this.futuresInAccountPercentage = futuresInAccountPercentage;
        StockInAccountPercentage = stockInAccountPercentage;
        this.riskType = riskType;
    }

    public RiskProfileDto() {}
}
