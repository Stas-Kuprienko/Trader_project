package com.anastasia.trade_project.dto;

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

    private String broker;

    private String ticker;

    private String board;

    private double price;

    private int quantity;

    private String direction;

    private String status;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3})$")
    private String created;


    @Builder
    public OrderDto(int orderId,
                    String clientId,
                    String broker,
                    String ticker,
                    String board,
                    double price,
                    int quantity,
                    String direction,
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
