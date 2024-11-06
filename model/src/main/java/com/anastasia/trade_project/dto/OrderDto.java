package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Broker;
import com.anastasia.trade_project.enums.Direction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class OrderDto {

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("client_id")
    private String clientId;

    private Broker broker;

    private String ticker;

    private Board board;

    private double price;

    private int quantity;

    private Direction direction;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String created;


    @Builder
    public OrderDto(int orderId,
                    String clientId,
                    Broker broker,
                    String ticker,
                    Board board,
                    double price,
                    int quantity,
                    Direction direction,
                    String status,
                    String created) {
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

    public OrderDto() {}
}
