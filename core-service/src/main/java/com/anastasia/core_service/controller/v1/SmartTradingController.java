package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.service.SmartTradingService;
import com.anastasia.trade_project.dto.StrategyDefinition;
import com.anastasia.trade_project.forms.SmartSubscriptionRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

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
    public Mono<Void> subscribe(@RequestBody @Valid SmartSubscriptionRequest request) {
        //TODO temporary, just for test
        UUID userId = UUID.fromString("25fca6c8-08fe-4bbe-aa5e-dcb26aa67e26");

        return smartTradingService.subscribe(userId, request);
    }
}
