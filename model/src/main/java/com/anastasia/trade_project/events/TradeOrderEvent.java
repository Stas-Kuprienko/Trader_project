package com.anastasia.trade_project.events;

import com.anastasia.trade_project.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TradeOrderEvent implements Serializable {

    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("transaction_id")
    private int transactionId;

    private Broker broker;

    @JsonProperty("client_id")
    private String clientId;

    private String ticker;

    private Board board;

    private double price;

    private long quantity;

    private Direction direction;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;


    @Builder
    public TradeOrderEvent(UUID accountId,
                           int transactionId,
                           Broker broker,
                           String clientId,
                           String ticker,
                           Board board,
                           double price,
                           long quantity,
                           Direction direction,
                           LocalDateTime time) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.broker = broker;
        this.clientId = clientId;
        this.ticker = ticker;
        this.board = board;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.time = time;
    }

    public TradeOrderEvent() {}
}
