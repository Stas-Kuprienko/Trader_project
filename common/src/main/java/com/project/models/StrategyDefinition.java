package com.project.models;

import com.project.enums.TradeScope;
import lombok.Builder;
import lombok.Data;

@Data
public class StrategyDefinition {

    private String name;
    private TradeScope tradeScope;


    @Builder
    public StrategyDefinition(String name, TradeScope tradeScope) {
        this.name = name;
        this.tradeScope = tradeScope;
    }

    public StrategyDefinition() {}
}
