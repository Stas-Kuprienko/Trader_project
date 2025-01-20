package com.project.models;

import com.project.enums.Board;
import com.project.enums.Broker;
import com.project.enums.ExchangeMarket;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class SmartSubscription {

    private UUID accountId;
    private String clientId;
    private Broker broker;
    private String ticker;
    private Board board;
    private ExchangeMarket exchange;
    private StrategyDefinition strategyDefinition;


    @Builder
    public SmartSubscription(UUID accountId,
                             String clientId,
                             Broker broker,
                             String ticker,
                             Board board,
                             ExchangeMarket exchange,
                             StrategyDefinition strategyDefinition) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.broker = broker;
        this.ticker = ticker;
        this.board = board;
        this.exchange = exchange;
        this.strategyDefinition = strategyDefinition;
    }

    public SmartSubscription() {}
}
