package com.project.forms;

import com.project.enums.ExchangeMarket;
import com.project.enums.Market;
import com.project.models.StrategyDefinition;
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

    @NotNull
    @JsonProperty("strategy_definition")
    private StrategyDefinition strategyDefinition;


    @Builder
    public SmartSubscriptionRequest(String ticker,
                                    Market market,
                                    ExchangeMarket exchange,
                                    UUID accountId,
                                    StrategyDefinition strategyDefinition) {
        this.ticker = ticker;
        this.market = market;
        this.exchange = exchange;
        this.accountId = accountId;
        this.strategyDefinition = strategyDefinition;
    }

    public SmartSubscriptionRequest() {}
}
