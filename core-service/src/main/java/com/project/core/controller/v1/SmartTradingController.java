package com.project.core.controller.v1;

import com.project.core.domain.market.MarketDataDispatcher;
import com.project.core.service.SmartTradingService;
import com.project.core.utility.JwtUtility;
import com.project.forms.SmartSubscriptionRequest;
import com.project.market.Securities;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/smart")
public class SmartTradingController {

    private final SmartTradingService smartTradingService;
    private final MarketDataDispatcher marketDataDispatcher;

    @Autowired
    public SmartTradingController(SmartTradingService smartTradingService, MarketDataDispatcher marketDataDispatcher) {
        this.smartTradingService = smartTradingService;
        this.marketDataDispatcher = marketDataDispatcher;
    }


    @GetMapping("/strategies")
    public Flux<String> strategies() {
        return smartTradingService.strategies();
    }


    @PostMapping("/subscription")
    public Mono<Void> subscribe(@AuthenticationPrincipal Jwt jwt,
                                @RequestBody @Valid SmartSubscriptionRequest request) {
        return Mono.zip(marketDataDispatcher
                                .marketDataProvider(request.getExchange())
                                .getSecurities(request.getTicker(), request.getMarket()),
                        Mono.just(JwtUtility.extractUserId(jwt)))
                .flatMap(tuple -> {
                    Securities securities = tuple.getT1();
                    UUID userId = tuple.getT2();
                    return smartTradingService
                            .subscribe(userId, request.getAccountId(), securities, request.getStrategyDefinition());
                });
    }


    @DeleteMapping("/subscription")
    public Mono<Void> unsubscribe(@AuthenticationPrincipal Jwt jwt,
                                  @RequestBody @Valid SmartSubscriptionRequest request) {
        return Mono.zip(marketDataDispatcher
                                .marketDataProvider(request.getExchange())
                                .getSecurities(request.getTicker(), request.getMarket()),
                        Mono.just(JwtUtility.extractUserId(jwt)))
                .flatMap(tuple -> {
                    Securities securities = tuple.getT1();
                    UUID userId = tuple.getT2();
                    return smartTradingService
                            .subscribe(userId, request.getAccountId(), securities, request.getStrategyDefinition());
                });
    }


    
}
