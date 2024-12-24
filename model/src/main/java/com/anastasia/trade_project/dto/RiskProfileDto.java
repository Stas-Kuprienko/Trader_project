package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonProperty("created_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Pattern(regexp = "(\\d{4})-(\\d{2})-(\\d{2})")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    @Pattern(regexp = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})")
    private String updatedAt;


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
