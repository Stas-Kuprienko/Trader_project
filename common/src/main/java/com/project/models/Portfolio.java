package com.project.models;

import com.project.enums.Broker;
import com.project.enums.Market;
import com.project.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Portfolio {

    private String clientId;
    private Broker broker;
    private double balance;
    private List<Position> positions;
    private LocalDateTime relevanceAt;

    @Builder
    public Portfolio(String clientId,
                     Broker broker,
                     double balance,
                     List<Position> positions,
                     LocalDateTime relevanceAt) {
        this.clientId = clientId;
        this.broker = broker;
        this.balance = balance;
        this.positions = positions;
        this.relevanceAt = relevanceAt;
    }

    public Portfolio() {}


    public record Position(String ticker,
                           Market market,
                           double price,
                           double totalCost,
                           int quantity,
                           Currency currency,
                           double profit) {}
}
