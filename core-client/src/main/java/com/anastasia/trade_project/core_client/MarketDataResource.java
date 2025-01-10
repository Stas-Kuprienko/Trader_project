package com.anastasia.trade_project.core_client;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.market.Futures;
import com.anastasia.trade_project.market.MarketPage;
import com.anastasia.trade_project.market.Stock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Optional;

public class MarketDataResource extends HttpError404Handler {

    private static final String resourceUrl = "/market-data";

    private static final String STOCK_LIST = "/%s/stocks";
    private static final String STOCK = "/%s/stocks/%s";
    private static final String FUTURES_LIST = "/%s/futures";
    private static final String FUTURES = "/%s/futures/%s";

    private static final String PAGE_PARAM = "page";
    private static final String COUNT_PARAM = "count";
    private static final String SORT_BY_PARAM = "sort-by";
    private static final String SORT_DIR_PARAM = "sort-dir";

    private final RestClient restClient;


    MarketDataResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public List<Stock> stockList(ExchangeMarket exchange, MarketPage page) {
        return restClient.get()
                .uri(build -> build
                        .path(STOCK_LIST.formatted(exchange))
                        .queryParam(PAGE_PARAM, page.getPage())
                        .queryParam(COUNT_PARAM, page.getCount())
                        .queryParam(SORT_BY_PARAM, page.getSort())
                        .queryParam(SORT_DIR_PARAM, page.getDirection())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<Stock> stock(ExchangeMarket exchange, String ticker) {
        return catch404(
                () -> restClient.get()
                .uri(STOCK.formatted(exchange, ticker))
                .retrieve()
                .body(Stock.class));
    }

    public List<Futures> futuresList(ExchangeMarket exchange, MarketPage page) {
        return restClient.get()
                .uri(build -> build
                        .path(FUTURES_LIST.formatted(exchange))
                        .queryParam(PAGE_PARAM, page.getPage())
                        .queryParam(COUNT_PARAM, page.getCount())
                        .queryParam(SORT_BY_PARAM, page.getSort())
                        .queryParam(SORT_DIR_PARAM, page.getDirection())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<Futures> futures(ExchangeMarket exchange, String ticker) {
        return catch404(
                () -> restClient.get()
                .uri(FUTURES.formatted(exchange, ticker))
                .retrieve()
                .body(Futures.class));
    }
}
