package com.trader_project.core_service.controller.v1;

import com.trader_project.core_service.domain.market.MarketDataDispatcher;
import com.trade_project.enums.ExchangeMarket;
import com.trade_project.forms.ErrorDto;
import com.trade_project.market.Futures;
import com.trade_project.market.MarketPage;
import com.trade_project.market.Stock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Market Data")
@RestController
@RequestMapping("/api/v1/market-data")
public class MarketDataController {

    private final MarketDataDispatcher marketDataDispatcher;

    @Autowired
    public MarketDataController(MarketDataDispatcher marketDataDispatcher) {
        this.marketDataDispatcher = marketDataDispatcher;
    }


    @Operation(summary = "All stocks for exchange")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Stock list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Stock.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping("/{exchange}/stocks")
    public Flux<Stock> getStockList(@PathVariable("exchange") String exchange,
                                    @RequestParam("page") Integer page,
                                    @RequestParam("count") Integer count,
                                    @RequestParam(value = "sort-by", defaultValue = "VOLUME") String sorting,
                                    @RequestParam(value = "sort-dir", required = false) String direction) {

        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        MarketPage marketPage = new MarketPage(page, count, sorting, direction);
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .stocksList(marketPage);
    }


    @Operation(summary = "Find stock by ticker")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Stock data", content = @Content(schema = @Schema(implementation = Stock.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Stock is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping("/{exchange}/stocks/{ticker}")
    public Mono<Stock> getStock(@PathVariable("exchange") String exchange,
                                @PathVariable("ticker") String ticker) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .getStock(ticker);
    }


    @Operation(summary = "All futures for exchange")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Futures list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Futures.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping("/{exchange}/futures")
    public Flux<Futures> getFuturesList(@PathVariable("exchange") String exchange,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("count") Integer count,
                                        @RequestParam(value = "sort-by", defaultValue = "VOLUME") String sorting,
                                        @RequestParam(value = "sort-dir", required = false) String direction) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        MarketPage marketPage = new MarketPage(page, count, sorting, direction);
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .futuresList(marketPage);
    }


    @Operation(summary = "Find futures by ticker")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Futures data", content = @Content(schema = @Schema(implementation = Futures.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Futures is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping("/{exchange}/futures/{ticker}")
    public Mono<Futures> getFutures(@PathVariable("exchange") String exchange,
                                    @PathVariable("ticker") String ticker) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange.toUpperCase());
        return marketDataDispatcher
                .marketDataProvider(exchangeMarket)
                .getFutures(ticker);
    }
}
