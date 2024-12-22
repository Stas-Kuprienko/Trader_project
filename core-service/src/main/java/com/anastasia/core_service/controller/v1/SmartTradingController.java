package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.service.SmartTradingService;
import com.anastasia.trade_project.dto.StrategyDefinition;
import com.anastasia.trade_project.dto.form.SmartSubscriptionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/smart")
public class SmartTradingController {

    private final SmartTradingService smartTradingService;

    @Autowired
    public SmartTradingController(SmartTradingService smartTradingService) {
        this.smartTradingService = smartTradingService;
    }


    @GetMapping("/strategies")
    public Flux<StrategyDefinition> strategies() {
        return smartTradingService.strategies();
    }

    @PostMapping("/subscribe")
    public Mono<Void> subscribe(@RequestBody SmartSubscriptionRequest request) {
        //TODO temporary, just for test
        Long userId = 1L;

        return smartTradingService.subscribe(userId, request);
    }
}
