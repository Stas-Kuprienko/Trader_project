package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.domain.market.MarketProvider;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MoexMarketProvider implements MarketProvider {

    private static final String STOCK_URL = "/engines/stock/markets/shares/boards/tqbr/securities/%s";
    private static final String STOCK_LIST_URL = "/engines/stock/markets/shares/boards/tqbr/securities";
    private static final String FUTURES_URL = "/engines/futures/markets/forts/securities/%s";
    private static final String FUTURES_LIST_URL = "/engines/futures/markets/forts/securities";

    private final MoexXmlUtility xmlParser;
    private final WebClient webClient;


    @Autowired
    public MoexMarketProvider(MoexXmlUtility xmlParser,
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
    public Flux<Stock> stocksList(int page, int count, Sort sort) {

        return null;
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
    public Flux<Futures> futuresList(int page, int count, Sort sort) {

        return null;
    }

    @Override
    public Mono<Futures> getFutures(String ticker) {

        return null;
    }
}
