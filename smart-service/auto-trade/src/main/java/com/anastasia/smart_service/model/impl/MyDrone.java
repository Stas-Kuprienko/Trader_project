package com.anastasia.smart_service.model.impl;

import com.anastasia.smart_service.Smart;
import com.anastasia.smart_service.domain.strategy.TradeStrategy;
import java.util.HashSet;
import java.util.Set;

public class MyDrone {

    private final TradeStrategy strategy;
    private final Set<Smart.Account> subscribers;

    public MyDrone(TradeStrategy strategy) {
        this.strategy = strategy;
        subscribers = new HashSet<>();
    }
}
