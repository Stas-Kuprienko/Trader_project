package com.anastasia.core_service.datasource.cache.redis;

import com.anastasia.core_service.datasource.cache.MarketDataRepository;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.market.Futures;
import com.anastasia.trade_project.market.Stock;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class MarketDataRepositoryRedis implements MarketDataRepository {

    private static final String STOCK_KEY = "_STOCK";
    private static final String FUTURES_KEY = "_FUTURES";

    private final ReactiveRedisTemplate<String, Stock> stockRedisTemplate;
    private final ReactiveRedisTemplate<String, Futures> futuresRedisTemplate;


    @Autowired
    public MarketDataRepositoryRedis(@Qualifier("stockRedisTemplate") ReactiveRedisTemplate<String, Stock> stockRedisTemplate,
                                     @Qualifier("futuresRedisTemplate") ReactiveRedisTemplate<String, Futures> futuresRedisTemplate) {
        this.stockRedisTemplate = stockRedisTemplate;
        this.futuresRedisTemplate = futuresRedisTemplate;
    }


    @PostConstruct
    public void init() {
        for (ExchangeMarket e : ExchangeMarket.values()) {

            String stockKey = e + STOCK_KEY;
            Duration stockDuration = Duration.of(5, ChronoUnit.MINUTES);
            stockRedisTemplate.expire(stockKey, stockDuration);
            log.info("Cache duration for {} key is set to {}", stockKey, stockDuration);

            String futuresKey = e + FUTURES_KEY;
            Duration futuresDuration = Duration.of(5, ChronoUnit.MINUTES);
            futuresRedisTemplate.expire(futuresKey, futuresDuration);
            log.info("Cache duration for {} key is set to {}", futuresKey, futuresDuration);
        }
    }


    @Override
    public Mono<Boolean> putStock(Stock stock) {
        return stockRedisTemplate
                .opsForHash()
                .put(stock.getExchange() + STOCK_KEY, stock.getTicker(), stock)
                .doOnNext(b -> log.debug("The object is cached: {}", stock));
    }

    @Override
    public Mono<Boolean> putStockList(ExchangeMarket exchange, List<Stock> stockList) {
        var opsForHash = stockRedisTemplate.opsForHash();
        return Mono.just(stockList)
                .map(list -> {
                    Map<String, Stock> map = new HashMap<>();
                    list.forEach(stock -> map.put(stock.getTicker(), stock));
                    return map;
                })
                .flatMap(stockMap -> opsForHash.putAll(exchange + STOCK_KEY, stockMap))
                .doOnNext(b -> log.debug("The objects is cached: {}", stockList));
    }

    @Override
    public Mono<Boolean> putFutures(Futures futures) {
        return futuresRedisTemplate
                .opsForHash()
                .put(futures.getExchange() + FUTURES_KEY, futures.getTicker(), futures)
                .doOnNext(b -> log.debug("The object is cached: {}", futures));
    }

    @Override
    public Mono<Boolean> putFuturesList(ExchangeMarket exchange, List<Futures> futuresList) {
        var opsForHash = futuresRedisTemplate.opsForHash();
        return Mono.just(futuresList)
                .map(list -> {
                    Map<String, Futures> map = new HashMap<>();
                    list.forEach(futures -> map.put(futures.getTicker(), futures));
                    return map;
                })
                .flatMap(futuresMap -> opsForHash.putAll(exchange + FUTURES_KEY, futuresMap))
                .doOnNext(b -> log.debug("The objects is cached: {}", futuresList));
    }

    @Override
    public Mono<Stock> getStock(ExchangeMarket exchange, String ticker) {
        return stockRedisTemplate.opsForHash()
                .get(exchange + STOCK_KEY, ticker)
                .map(Stock.class::cast)
                .doOnNext(stock -> log.debug("The object is retrieved: {}", stock));
    }

    @Override
    public Mono<List<Stock>> getStockList(ExchangeMarket exchange) {
        return stockRedisTemplate.opsForHash()
                .entries(exchange + STOCK_KEY)
                .map(e -> (Stock) e.getValue())
                .collectList()
                .doOnNext(stockList -> log.info("{} Stock objects is retrieved.", stockList.size()));
    }

    @Override
    public Mono<Futures> getFutures(ExchangeMarket exchange, String ticker) {
        return futuresRedisTemplate.opsForHash()
                .get(exchange + FUTURES_KEY, ticker)
                .map(Futures.class::cast)
                .doOnNext(futures -> log.debug("The object is retrieved: {}", futures));
    }

    @Override
    public Mono<List<Futures>> getFuturesList(ExchangeMarket exchange) {
        return futuresRedisTemplate.opsForHash()
                .entries(exchange + FUTURES_KEY)
                .map(e -> (Futures) e.getValue())
                .collectList()
                .doOnNext(futuresList -> log.info("{} Futures objects is retrieved.", futuresList.size()));
    }
}
