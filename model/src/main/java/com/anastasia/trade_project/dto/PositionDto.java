package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class PositionDto {

    private String ticker;

    private String market;

    private double price;

    @JsonProperty("total_cost")
    private double totalCost;

    private int quantity;

    private String currency;

    private double profit;


    @Builder
    public PositionDto(String ticker,
                       String market,
                       double price,
                       double totalCost,
                       int quantity,
                       String currency,
                       double profit) {
        this.ticker = ticker;
        this.market = market;
        this.price = price;
        this.totalCost = totalCost;
        this.quantity = quantity;
        this.currency = currency;
        this.profit = profit;
    }

    public PositionDto() {}
}
