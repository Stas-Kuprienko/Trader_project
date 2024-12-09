package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.domain.smart.SmartServiceClient;
import com.anastasia.trade_project.dto.StrategyDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/smart")
public class AutoTradingController {

    private final SmartServiceClient smartServiceClient;

    @Autowired
    public AutoTradingController(SmartServiceClient smartServiceClient) {
        this.smartServiceClient = smartServiceClient;
    }


    @GetMapping("/strategies")
    public Flux<StrategyDefinition> strategies() {
        return smartServiceClient.strategies();
    }
}
