package core_client_reactive;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.MarketPage;
import com.anastasia.trade_project.markets.Stock;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    private final WebClient webClient;


    MarketDataResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public Flux<Stock> stockList(ExchangeMarket exchange, MarketPage page) {
        return webClient.get()
                .uri(build -> build
                        .path(STOCK_LIST.formatted(exchange))
                        .queryParam(PAGE_PARAM, page.getPage())
                        .queryParam(COUNT_PARAM, page.getCount())
                        .queryParam(SORT_BY_PARAM, page.getSort())
                        .queryParam(SORT_DIR_PARAM, page.getDirection())
                        .build())
                .retrieve()
                .bodyToFlux(Stock.class);
    }

    public Mono<Stock> stock(ExchangeMarket exchange, String ticker) {
        return process(() -> webClient.get()
                .uri(STOCK.formatted(exchange, ticker))
                .retrieve()
                .bodyToMono(Stock.class));
    }

    public Flux<Futures> futuresList(ExchangeMarket exchange, MarketPage page) {
        return webClient.get()
                .uri(build -> build
                        .path(FUTURES_LIST.formatted(exchange))
                        .queryParam(PAGE_PARAM, page.getPage())
                        .queryParam(COUNT_PARAM, page.getCount())
                        .queryParam(SORT_BY_PARAM, page.getSort())
                        .queryParam(SORT_DIR_PARAM, page.getDirection())
                        .build())
                .retrieve()
                .bodyToFlux(Futures.class);
    }

    public Mono<Futures> futures(ExchangeMarket exchange, String ticker) {
        return process(() -> webClient.get()
                .uri(FUTURES.formatted(exchange, ticker))
                .retrieve()
                .bodyToMono(Futures.class));
    }
}
