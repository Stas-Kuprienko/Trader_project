package com.anastasia.trade_project.enums;

public enum Broker {

    Finam("Финам", ExchangeMarket.MOEX);

    
    private final String title;
    private final ExchangeMarket exchange;


    Broker(String title, ExchangeMarket exchange) {
        this.title = title;
        this.exchange = exchange;
    }


    public String title() {
        return title;
    }

    public ExchangeMarket exchangeMarket() {
        return exchange;
    }
}
