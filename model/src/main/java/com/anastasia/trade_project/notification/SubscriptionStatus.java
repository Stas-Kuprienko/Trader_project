package com.anastasia.trade_project.notification;

import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Broker;
import com.anastasia.trade_project.enums.TradeScope;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubscriptionStatus {

    @JsonIgnore
    public static final Option SUBSCRIBE = Option.SUBSCRIBE;
    @JsonIgnore
    public static final Option UNSUBSCRIBE = Option.UNSUBSCRIBE;

    @JsonProperty("trade_strategy")
    private String tradeStrategy;

    @JsonProperty("trade_scope")
    private TradeScope tradeScope;

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
    public SubscriptionStatus(String tradeStrategy,
                              TradeScope tradeScope,
                              Option option,
                              boolean success,
                              Broker broker,
                              String clientId,
                              String ticker,
                              Board board,
                              LocalDateTime time) {
        this.tradeStrategy = tradeStrategy;
        this.tradeScope = tradeScope;
        this.option = option;
        this.success = success;
        this.broker = broker;
        this.clientId = clientId;
        this.ticker = ticker;
        this.board = board;
        this.time = time;
    }

    public SubscriptionStatus() {}


    public enum Option {
        SUBSCRIBE, UNSUBSCRIBE
    }
}
