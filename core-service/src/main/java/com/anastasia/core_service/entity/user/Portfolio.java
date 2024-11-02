package com.anastasia.core_service.entity.user;

import com.anastasia.core_service.entity.enums.Broker;
import com.anastasia.core_service.entity.enums.Market;
import com.anastasia.core_service.entity.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
