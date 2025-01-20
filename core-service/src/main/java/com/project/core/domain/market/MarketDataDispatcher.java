package com.project.core.domain.market;

import com.project.exception.ConfigurationException;
import com.project.enums.ExchangeMarket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MarketDataDispatcher {

    private final Map<ExchangeMarket, MarketDataProvider> providers;

    @Autowired
    public MarketDataDispatcher(ConfigurableApplicationContext applicationContext) {
        providers = collectProvider(applicationContext);
    }


    public MarketDataProvider marketDataProvider(ExchangeMarket exchange) {
        MarketDataProvider provider = providers.get(exchange);
        if (provider == null) {
            throw new ConfigurationException("Not found MarketDataProvider for ExchangeMarket: " + exchange);
        }
        return provider;
    }


    protected Map<ExchangeMarket, MarketDataProvider> collectProvider(ConfigurableApplicationContext applicationContext) {
        Map<ExchangeMarket, MarketDataProvider> providerMap = new HashMap<>();
        applicationContext.getBeansOfType(MarketDataProvider.class)
                .forEach((key, value) -> {
                    MarketData marketData = value.getClass().getAnnotation(MarketData.class);
                    if (marketData != null) {
                        providerMap.put(marketData.exchange(), value);
                        log.info("MarketDataProvider for {} is registered", marketData.exchange());
                    }
                });
        return providerMap;
    }
}
