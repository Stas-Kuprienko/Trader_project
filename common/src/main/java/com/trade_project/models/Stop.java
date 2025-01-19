package com.trade_project.models;

import com.trade_project.enums.Board;
import com.trade_project.enums.Broker;
import com.trade_project.enums.Direction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class Stop {

    private int stopId;
    private String clientId;
    private Broker broker;
    private String ticker;
    private Board board;
    private int quantity;
    private BigDecimal price;
    private Direction direction;
    private Type type;


    @Builder
    public Stop(int stopId,
                String clientId,
                Broker broker,
                String ticker,
                Board board,
                int quantity,
                BigDecimal price,
                Direction direction,
                Type type) {
        this.stopId = stopId;
        this.clientId = clientId;
        this.broker = broker;
        this.ticker = ticker;
        this.board = board;
        this.quantity = quantity;
        this.price = price;
        this.direction = direction;
        this.type = type;
    }

    public Stop() {}


    public enum Type {
        STOP_LOSS,
        TAKE_PROFIT
    }
}