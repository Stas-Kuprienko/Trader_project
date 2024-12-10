package com.anastasia.telegram_bot.service.impl;

import com.anastasia.telegram_bot.datasource.MarketDataRepository;
import com.anastasia.telegram_bot.service.MarketDataService;
import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import com.anastasia.trade_project.core_client.util.PaginationUtility;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.MarketPage;
import com.anastasia.trade_project.markets.Stock;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final MarketDataRepository marketDataRepository;
    private final CoreServiceClientV1 coreServiceClient;

    @Autowired
    public MarketDataServiceImpl(MarketDataRepository marketDataRepository, CoreServiceClientV1 coreServiceClient) {
        this.marketDataRepository = marketDataRepository;
        this.coreServiceClient = coreServiceClient;
    }


    @Override
    public Flux<Stock> stockList(ExchangeMarket exchange, MarketPage page) {
        return marketDataRepository
                .getStockList(exchange)
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.just(coreServiceClient.MARKET.stockList(exchange, page));
                    } else {
                        return Mono.just(list);
                    }
                })
                .flatMapIterable(list -> PaginationUtility
                        .findPage(
                                PaginationUtility.sorting(list, page.getSort(), page.getDirection()),
                                page.getPage(),
                                page.getCount()));
    }

    @Override
    public Mono<Stock> stock(ExchangeMarket exchange, String ticker) {
        return marketDataRepository
                .getStock(exchange, ticker)
                .switchIfEmpty(Mono.just(coreServiceClient.MARKET
                        .stock(exchange, ticker)
                        .orElseThrow(NotFoundException::new)));
    }

    @Override
    public Flux<Futures> futuresList(ExchangeMarket exchange, MarketPage page) {
        return marketDataRepository
                .getFuturesList(exchange)
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.just(coreServiceClient.MARKET.futuresList(exchange, page));
                    } else {
                        return Mono.just(list);
                    }
                })
                .flatMapIterable(list -> PaginationUtility
                        .findPage(
                                PaginationUtility.sorting(list, page.getSort(), page.getDirection()),
                                page.getPage(),
                                page.getCount()));
    }

    @Override
    public Mono<Futures> futures(ExchangeMarket exchange, String ticker) {
        return marketDataRepository
                .getFutures(exchange, ticker)
                .switchIfEmpty(Mono.just(coreServiceClient.MARKET
                        .futures(exchange, ticker)
                        .orElseThrow(NotFoundException::new)));
    }
}
