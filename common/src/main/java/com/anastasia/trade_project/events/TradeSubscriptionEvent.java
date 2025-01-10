package com.anastasia.trade_project.events;

import com.anastasia.trade_project.models.StrategyDefinition;
import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Broker;
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

    @JsonProperty("strategy_definition")
    private StrategyDefinition strategyDefinition;

    private Option option;

    private boolean success;

    private Broker broker;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("user_id")
    private UUID userId;

    private String ticker;

    private Board board;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;


    @Builder
    public TradeSubscriptionEvent(StrategyDefinition strategyDefinition,
                                  Option option,
                                  boolean success,
                                  Broker broker,
                                  String clientId,
                                  UUID userId,
                                  String ticker,
                                  Board board,
                                  LocalDateTime time) {
        this.strategyDefinition = strategyDefinition;
        this.option = option;
        this.success = success;
        this.broker = broker;
        this.clientId = clientId;
        this.userId = userId;
        this.ticker = ticker;
        this.board = board;
        this.time = time;
    }

    public TradeSubscriptionEvent() {}


    public enum Option {
        SUBSCRIBE, UNSUBSCRIBE
    }
}
