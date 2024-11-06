package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class TradeNotification {

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String id;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    private String login;

    private Broker broker;

    @JsonProperty("client_id")
    private String clientId;

    private ExchangeMarket exchangeMarket;

    private String ticker;

    private Board board;

    private Market market;

    private double price;

    private int quantity;

    private Direction direction;

    private Currency currency;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String time;


    @Builder
    public TradeNotification(String id,
                             String login,
                             Broker broker,
                             String clientId,
                             ExchangeMarket exchangeMarket,
                             String ticker,
                             Board board,
                             Market market,
                             double price,
                             int quantity,
                             Direction direction,
                             Currency currency,
                             String time) {
        this.id = id;
        this.login = login;
        this.broker = broker;
        this.clientId = clientId;
        this.exchangeMarket = exchangeMarket;
        this.ticker = ticker;
        this.board = board;
        this.market = market;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.currency = currency;
        this.time = time;
    }

    public TradeNotification() {}
}
