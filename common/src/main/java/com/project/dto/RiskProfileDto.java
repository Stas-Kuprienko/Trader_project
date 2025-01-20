package com.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class RiskProfileDto {

    private UUID id;

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

    @JsonProperty("created_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private String updatedAt;


    @Builder
    public RiskProfileDto(UUID id,
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
