package com.anastasia.telegram_bot.datasource;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import reactor.core.publisher.Mono;
import java.util.List;

public interface MarketDataRepository {

    Mono<Stock> getStock(ExchangeMarket exchange, String ticker);

    Mono<List<Stock>> getStockList(ExchangeMarket exchange);

    Mono<Futures> getFutures(ExchangeMarket exchange, String ticker);

    Mono<List<Futures>> getFuturesList(ExchangeMarket exchange);
}
