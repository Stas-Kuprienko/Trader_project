package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.datasource.cache.MarketDataCache;
import com.anastasia.core_service.domain.market.MarketData;
import com.anastasia.core_service.domain.market.MarketDataProvider;
import com.anastasia.core_service.utility.PaginationUtility;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Sorting;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@MarketData(exchange = ExchangeMarket.MOEX)
public class MoexMarketDataProvider implements MarketDataProvider {

    private static final String STOCK_URL = "/engines/stock/markets/shares/boards/tqbr/securities/%s";
    private static final String STOCK_LIST_URL = "/engines/stock/markets/shares/boards/tqbr/securities";
    private static final String FUTURES_URL = "/engines/futures/markets/forts/securities/%s";
    private static final String FUTURES_LIST_URL = "/engines/futures/markets/forts/securities";

    private static final int increasedMaxInMemorySize = 16 * 1024 * 1024;

    private final MarketDataCache marketDataCache;
    private final MoexXmlUtility xmlUtility;
    private final WebClient webClient;


    @Autowired
    public MoexMarketDataProvider(MarketDataCache marketDataCache,
                                  MoexXmlUtility xmlUtility,
                                  @Value("${project.exchange.moex.url}") String moexBaseUrl) {
        this.marketDataCache = marketDataCache;
        this.xmlUtility = xmlUtility;
        this.webClient = WebClient.builder()
                .baseUrl(moexBaseUrl)
                .build();
    }


    @Override
    public ExchangeMarket exchange() {
        return ExchangeMarket.MOEX;
    }


    @Override
    public Flux<Stock> stocksList(int page, int count, Sorting sorting, Sorting.Direction direction) {
        return marketDataCache
                .getStockList(ExchangeMarket.MOEX)
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return webClient.mutate()
                                .codecs(config -> config
                                        .defaultCodecs()
                                        .maxInMemorySize(increasedMaxInMemorySize))
                                .build()
                                .get()
                                .uri(STOCK_LIST_URL)
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(xmlUtility::parse)
                                .map(document -> {
                                    Iterator<Map<String, Object>> securities = document.securitiesData().iterator();
                                    Iterator<Map<String, Object>> marketData = document.marketData().iterator();
                                    List<Stock> stocks = new ArrayList<>();
                                    while (securities.hasNext()) {
                                        stocks.add(xmlUtility.stock(securities.next(), marketData.next()));
                                    }
                                    return stocks;
                                })
                                .doOnNext(stocks -> marketDataCache.putStockList(stocks).subscribe());
                    } else {
                        return Mono.just(list);
                    }
                })
                .flatMapIterable(list -> PaginationUtility.findPage(
                        PaginationUtility.sorting(list, sorting, direction), page, count));
    }


    @Override
    public Mono<Stock> getStock(String ticker) {
        return marketDataCache
                .getStock(ExchangeMarket.MOEX, ticker)
                .switchIfEmpty(webClient
                        .get()
                        .uri(STOCK_URL.formatted(ticker))
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(xmlUtility::parse)
                        .map(document -> xmlUtility.stock(
                                document.securitiesData().getFirst(),
                                document.marketData().getFirst())));
    }


    @Override
    public Flux<Futures> futuresList(int page, int count, Sorting sorting, Sorting.Direction direction) {
        return marketDataCache
                .getFuturesList(ExchangeMarket.MOEX)
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return webClient.mutate()
                                .codecs(config -> config
                                        .defaultCodecs()
                                        .maxInMemorySize(increasedMaxInMemorySize))
                                .build()
                                .get()
                                .uri(FUTURES_LIST_URL)
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(xmlUtility::parse)
                                .map(document -> {
                                    Iterator<Map<String, Object>> securities = document.securitiesData().iterator();
                                    Iterator<Map<String, Object>> marketData = document.marketData().iterator();
                                    List<Futures> futuresList = new ArrayList<>();
                                    while (securities.hasNext()) {
                                        futuresList.add(xmlUtility.futures(securities.next(), marketData.next()));
                                    }
                                    return futuresList;
                                })
                                .doOnNext(futures -> marketDataCache.putFuturesList(futures).subscribe());
                    } else {
                        return Mono.just(list);
                    }
                })
                .flatMapIterable(list -> PaginationUtility.findPage(
                        PaginationUtility.sorting(list, sorting, direction), page, count));
    }


    @Override
    public Mono<Futures> getFutures(String ticker) {
        return marketDataCache
                .getFutures(ExchangeMarket.MOEX, ticker)
                .switchIfEmpty(webClient
                        .get()
                        .uri(FUTURES_URL.formatted(ticker))
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(xmlUtility::parse)
                        .map(document -> xmlUtility.futures(
                                document.securitiesData().getFirst(),
                                document.marketData().getFirst())));
    }
}
