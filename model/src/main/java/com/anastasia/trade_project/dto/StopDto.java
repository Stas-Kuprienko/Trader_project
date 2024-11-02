package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class StopDto {

    @JsonProperty("stop_id")
    private int stopId;

    @JsonProperty("client_id")
    private String clientId;

    private String broker;

    private String ticker;

    private String board;

    private int quantity;

    private double price;

    private String direction;

    private String type;


    @Builder
    public StopDto(int stopId,
                   String clientId,
                   String broker,
                   String ticker,
                   String board,
                   int quantity,
                   double price,
                   String direction,
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
