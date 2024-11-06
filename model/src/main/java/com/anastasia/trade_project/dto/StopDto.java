package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Broker;
import com.anastasia.trade_project.enums.Direction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class StopDto {

    @JsonProperty("stop_id")
    private int stopId;

    @JsonProperty("client_id")
    private String clientId;

    private Broker broker;

    private String ticker;

    private Board board;

    private int quantity;

    private double price;

    private Direction direction;

    private String type;


    @Builder
    public StopDto(int stopId,
                   String clientId,
                   Broker broker,
                   String ticker,
                   Board board,
                   int quantity,
                   double price,
                   Direction direction,
                   String type) {
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

    public StopDto() {}
}
