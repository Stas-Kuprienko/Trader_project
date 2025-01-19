package com.trader_project.smart_service.model.impl;

import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.strategy.TradeStrategy;
import com.trader_project.smart_service.model.Drone;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class MyDrone implements Drone {

    private final TradeStrategy strategy;
    private final Set<Smart.Account> subscribers;

    public MyDrone(TradeStrategy strategy) {
        this.strategy = strategy;
        subscribers = new HashSet<>();
    }


    @Override
    public void run(ExecutorService executorService) {

    }

    @Override
    public void addSubscriber(Smart.Account account) {

    }

    @Override
    public void removeSubscriber(Smart.Account account) {

    }

    @Override
    public boolean isEmpty() {
        return subscribers.isEmpty();
    }

    @Override
    public void stop(boolean forcibly) {

    }
}
