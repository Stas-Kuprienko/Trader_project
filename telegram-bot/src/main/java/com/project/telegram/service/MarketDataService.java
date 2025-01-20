package com.project.telegram.service;

import com.project.enums.ExchangeMarket;
import com.project.market.Futures;
import com.project.market.MarketPage;
import com.project.market.Stock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketDataService {

    Flux<Stock> stockList(ExchangeMarket exchange, MarketPage page);

    Mono<Stock> stock(ExchangeMarket exchange, String ticker);

    Flux<Futures> futuresList(ExchangeMarket exchange, MarketPage page);

    Mono<Futures> futures(ExchangeMarket exchange, String ticker);
}
