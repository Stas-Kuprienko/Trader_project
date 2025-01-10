package com.anastasia.telegram_bot.service.impl;

import com.anastasia.telegram_bot.service.MarketDataService;
import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.market.Futures;
import com.anastasia.trade_project.market.MarketPage;
import com.anastasia.trade_project.market.Stock;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final CoreServiceClientV1 coreServiceClient;

    @Autowired
    public MarketDataServiceImpl(CoreServiceClientV1 coreServiceClient) {
        this.coreServiceClient = coreServiceClient;
    }


    @Override
    public Flux<Stock> stockList(ExchangeMarket exchange, MarketPage page) {
        return Flux.fromIterable(coreServiceClient.MARKET
                        .stockList(exchange, page));
    }

    @Override
    public Mono<Stock> stock(ExchangeMarket exchange, String ticker) {
        return Mono.just(coreServiceClient.MARKET
                        .stock(exchange, ticker)
                        .orElseThrow(NotFoundException::new));
    }

    @Override
    public Flux<Futures> futuresList(ExchangeMarket exchange, MarketPage page) {
        return Flux.fromIterable(coreServiceClient.MARKET
                        .futuresList(exchange, page));
    }

    @Override
    public Mono<Futures> futures(ExchangeMarket exchange, String ticker) {
        return Mono.just(coreServiceClient.MARKET
                        .futures(exchange, ticker)
                        .orElseThrow(NotFoundException::new));
    }
}
