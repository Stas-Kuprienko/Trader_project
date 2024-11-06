package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Currency;
import com.anastasia.trade_project.enums.Market;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class PositionDto {

    private String ticker;

    private Market market;

    private double price;

    @JsonProperty("total_cost")
    private double totalCost;

    private int quantity;

    private Currency currency;

    private double profit;


    @Builder
    public PositionDto(String ticker,
                       Market market,
                       double price,
                       double totalCost,
                       int quantity,
                       Currency currency,
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
