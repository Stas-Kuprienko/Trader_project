package com.anastasia.smart_service.domain.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StrategyDispatcher {

    private final Map<String, TradeStrategy> strategies;
    private final List<String> strategyNames;

    @Autowired
    public StrategyDispatcher(ConfigurableApplicationContext applicationContext) {
        strategies = collectStrategies(applicationContext);
        strategyNames = collectNames(strategies);
    }


    public TradeStrategy tradeStrategy(String name) {
        var ts = strategies.get(name);
        if (ts == null) {
            throw new IllegalArgumentException("Trade strategy is not found with name: %s.".formatted(name));
        } else {
            return ts;
        }
    }

    public List<String> strategyList() {
        return strategyNames;
    }


    private Map<String, TradeStrategy> collectStrategies(ConfigurableApplicationContext applicationContext) {
        Map<String, TradeStrategy> strategyMap = new HashMap<>();
        applicationContext
                .getBeansOfType(TradeStrategy.class)
                .forEach((key, value) -> {
                    Strategy strategy = value.getClass().getAnnotation(Strategy.class);
                    if (strategy != null) {
                        strategyMap.put(strategy.name(), value);
                    }
                });
        return strategyMap;
    }

    private List<String> collectNames(Map<String, TradeStrategy> strategies) {
        List<String> strategyNameList = new ArrayList<>();
        strategies.forEach((key, value) -> {
            Strategy strategy = value.getClass().getAnnotation(Strategy.class);
            String strategyName = strategy.name();
            strategyNameList.add(strategyName);
        });
        return strategyNameList;
    }
}
