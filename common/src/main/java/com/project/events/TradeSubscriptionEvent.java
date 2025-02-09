package com.project.events;

import com.project.models.StrategyDefinition;
import com.project.enums.Board;
import com.project.enums.Broker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TradeSubscriptionEvent implements Serializable {

    @JsonIgnore
    public static final Option SUBSCRIBE = Option.SUBSCRIBE;
    @JsonIgnore
    public static final Option UNSUBSCRIBE = Option.UNSUBSCRIBE;

    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("strategy_definition")
    private StrategyDefinition strategyDefinition;

    private Option option;

    private boolean success;

    private Broker broker;

    @JsonProperty("client_id")
    private String clientId;

    private String ticker;

    private Board board;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;


    @Builder
    public TradeSubscriptionEvent(UUID accountId,
                                  StrategyDefinition strategyDefinition,
                                  Option option,
                                  boolean success,
                                  Broker broker,
                                  String clientId,
                                  String ticker,
                                  Board board,
                                  LocalDateTime time) {
        this.accountId = accountId;
        this.strategyDefinition = strategyDefinition;
        this.option = option;
        this.success = success;
        this.broker = broker;
        this.clientId = clientId;
        this.ticker = ticker;
        this.board = board;
        this.time = time;
    }

    public TradeSubscriptionEvent() {}


    public enum Option {
        SUBSCRIBE, UNSUBSCRIBE
    }
}
