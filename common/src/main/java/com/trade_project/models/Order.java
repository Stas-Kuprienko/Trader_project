package com.trade_project.models;

import com.trade_project.enums.Board;
import com.trade_project.enums.Broker;
import com.trade_project.enums.Direction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {

    private Integer orderId;
    private String clientId;
    private Broker broker;
    private String ticker;
    private Board board;
    private BigDecimal price;
    private int quantity;
    private Direction direction;
    private String status;
    private LocalDateTime created;


    @Builder
    public Order(int orderId,
                 String clientId,
                 Broker broker,
                 String ticker,
                 Board board,
                 BigDecimal price,
                 int quantity,
                 Direction direction,
                 String status,
                 LocalDateTime created) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.broker = broker;
        this.ticker = ticker;
        this.board = board;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.status = status;
        this.created = created;
    }

    public Order() {}
}