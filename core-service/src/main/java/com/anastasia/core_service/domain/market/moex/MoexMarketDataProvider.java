package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.domain.market.MarketData;
import com.anastasia.core_service.domain.market.MarketDataProvider;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Sorting;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Securities;
import com.anastasia.trade_project.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.*;

@MarketData(exchange = ExchangeMarket.MOEX)
public class MoexMarketDataProvider implements MarketDataProvider {

    private static final String STOCK_URL = "/engines/stock/markets/shares/boards/tqbr/securities/%s";
    private static final String STOCK_LIST_URL = "/engines/stock/markets/shares/boards/tqbr/securities";
    private static final String FUTURES_URL = "/engines/futures/markets/forts/securities/%s";
    private static final String FUTURES_LIST_URL = "/engines/futures/markets/forts/securities";

    private final MoexXmlUtility xmlParser;
    private final WebClient webClient;


    @Autowired
    public MoexMarketDataProvider(MoexXmlUtility xmlParser,
                                  @Value("${project.exchange.moex.url}") String moexBaseUrl) {
        this.xmlParser = xmlParser;
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
        //CACHE
        Comparator<Stock> comparator = Comparator.comparing(Securities::getTicker);
        return webClient
                .get()
                .uri(STOCK_LIST_URL)
                .retrieve()
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .flatMapIterable(document -> {
                    Iterator<Map<String, Object>> securities = document.securitiesData().iterator();
                    Iterator<Map<String, Object>> marketData = document.marketData().iterator();
                    List<Stock> stocks = new ArrayList<>();
                    while (securities.hasNext()) {
                        stocks.add(xmlParser.stock(securities.next(), marketData.next()));
                    }
                    return stocks;
                });
    }

    @Override
    public Mono<Stock> getStock(String ticker) {
        return webClient
                .get()
                .uri(STOCK_URL.formatted(ticker))
                .retrieve()
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .map(document -> xmlParser.stock(
                        document.securitiesData().getFirst(),
                        document.marketData().getFirst()));
    }

    @Override
    public Flux<Futures> futuresList(int page, int count, Sorting sorting, Sorting.Direction direction) {
        //CACHE
        return webClient
                .get()
                .uri(FUTURES_LIST_URL)
                .retrieve()
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .flatMapIterable(document -> {
                    Iterator<Map<String, Object>> securities = document.securitiesData().iterator();
                    Iterator<Map<String, Object>> marketData = document.marketData().iterator();
                    List<Futures> futuresList = new ArrayList<>();
                    while (securities.hasNext()) {
                        futuresList.add(xmlParser.futures(securities.next(), marketData.next()));
                    }
                    return futuresList;
                });
    }

    @Override
    public Mono<Futures> getFutures(String ticker) {
        return webClient
                .get()
                .uri(FUTURES_URL.formatted(ticker))
                .retrieve()
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .map(document -> xmlParser.futures(
                        document.securitiesData().getFirst(),
                        document.marketData().getFirst()));
    }
}
