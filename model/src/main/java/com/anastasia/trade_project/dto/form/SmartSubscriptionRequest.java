package com.anastasia.trade_project.dto.form;

import com.anastasia.trade_project.enums.*;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class SmartSubscriptionRequest {

    private String ticker;
    private Market market;
    private ExchangeMarket exchange;
    private UUID accountId;
    private String strategy;
    private TradeScope tradeScope;


    @Builder
    public SmartSubscriptionRequest(String ticker,
                                    Market market,
                                    ExchangeMarket exchange,
                                    UUID accountId,
                                    String strategy,
                                    TradeScope tradeScope) {
        this.ticker = ticker;
        this.market = market;
        this.exchange = exchange;
        this.accountId = accountId;
        this.strategy = strategy;
        this.tradeScope = tradeScope;
    }

    public SmartSubscriptionRequest() {}
}
