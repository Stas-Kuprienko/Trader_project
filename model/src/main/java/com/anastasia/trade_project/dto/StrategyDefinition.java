package com.anastasia.trade_project.dto;

import lombok.Data;

@Data
public class StrategyDefinition {

    private String name;
    private String description;


    public StrategyDefinition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public StrategyDefinition() {}
}
