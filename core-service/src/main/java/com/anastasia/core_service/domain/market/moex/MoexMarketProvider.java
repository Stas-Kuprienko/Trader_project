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

    private final MoexUrlDraft urlDraft;
    private final MoexXmlUtility xmlParser;
    private final WebClient webClient;

    @Autowired
    public MoexMarketProvider(MoexUrlDraft urlDraft, MoexXmlUtility xmlParser,
                              @Value("${project.exchange.moex.url}") String moexBaseUrl) {
        this.urlDraft = urlDraft;
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

        return null;
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
