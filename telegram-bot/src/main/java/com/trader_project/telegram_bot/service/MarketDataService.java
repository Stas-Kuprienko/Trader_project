package com.trader_project.telegram_bot.service;

import com.trade_project.enums.ExchangeMarket;
import com.trade_project.market.Futures;
import com.trade_project.market.MarketPage;
import com.trade_project.market.Stock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketDataService {

    Flux<Stock> stockList(ExchangeMarket exchange, MarketPage page);

    Mono<Stock> stock(ExchangeMarket exchange, String ticker);

    Flux<Futures> futuresList(ExchangeMarket exchange, MarketPage page);

    Mono<Futures> futures(ExchangeMarket exchange, String ticker);
}
