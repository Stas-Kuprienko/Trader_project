package com.anastasia.smart_service.domain.automation.impl;

import com.anastasia.smart_service.Smart;
import com.anastasia.smart_service.domain.automation.DroneLauncher;
import com.anastasia.smart_service.domain.strategy.TradeStrategy;
import com.anastasia.smart_service.model.Drone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
public class DroneLauncherImpl implements DroneLauncher {

    private final ExecutorService executorService;
    private final Map<TradeStrategy, Drone> running;


    @Autowired
    public DroneLauncherImpl(ExecutorService executorService) {
        this.executorService = executorService;
        running = new ConcurrentHashMap<>();
    }


    @Override
    public void run(Smart.SubscribeRequest request, Smart.SubscribeResponse response) {

    }
}
