package com.anastasia.trade_project.forms;

import com.anastasia.trade_project.enums.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class SmartSubscriptionRequest {

    @NotBlank
    private String ticker;

    @NotNull
    private Market market;

    @NotNull
    private ExchangeMarket exchange;

    @NotNull
    @JsonProperty("account_id")
    private UUID accountId;

    @NotBlank
    private String strategy;

    @NotNull
    @JsonProperty("trade_scope")
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
