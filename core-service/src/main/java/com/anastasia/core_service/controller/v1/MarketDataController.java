package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.domain.market.MarketDataDispatcher;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Sorting;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/market-data")
public class MarketDataController {

    private final MarketDataDispatcher marketDataDispatcher;

    @Autowired
    public MarketDataController(MarketDataDispatcher marketDataDispatcher) {
        this.marketDataDispatcher = marketDataDispatcher;
    }


    @GetMapping("/{exchange}/stocks}")
    public Flux<Stock> getStockList(@PathVariable("exchange") String exchange,
                                    @RequestParam("page") Integer page,
                                    @RequestParam("count") Integer count,
                                    @RequestParam(value = "sort-by", defaultValue = "VOLUME") String sorting,
                                    @RequestParam(value = "sort-dir", required = false) String direction) {

        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        Sorting securitiesSorting = Sorting.valueOf(sorting);
        Sorting.Direction sortDirection = direction != null ?
                Sorting.Direction.valueOf(direction) : securitiesSorting.defaultDirection;
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .stocksList(page, count, securitiesSorting, sortDirection);
    }


    @GetMapping("/{exchange}/stocks/{ticker}")
    public Mono<Stock> getStock(@PathVariable("exchange") String exchange,
                                                @PathVariable("ticker") String ticker) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .getStock(ticker);
    }


    @GetMapping("/{exchange}/futures")
    public Flux<Futures> getFuturesList(@PathVariable("exchange") String exchange,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("count") Integer count,
                                        @RequestParam(value = "sort-by", defaultValue = "VOLUME") String sorting,
                                        @RequestParam(value = "sort-dir", required = false) String direction) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        Sorting securitiesSorting = Sorting.valueOf(sorting);
        Sorting.Direction sortDirection = direction != null ?
                Sorting.Direction.valueOf(direction) : securitiesSorting.defaultDirection;
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .futuresList(page, count, securitiesSorting, sortDirection);
    }


    @GetMapping("/{exchange}/futures/{ticker}")
    public Mono<Futures> getFutures(@PathVariable("exchange") String exchange,
                                    @PathVariable("ticker") String ticker) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .getFutures(ticker);
    }
}
