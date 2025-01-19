package com.trader_project.smart_service.domain.automation.impl;

import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.automation.DroneLauncher;
import com.trader_project.smart_service.domain.strategy.TradeStrategy;
import com.trader_project.smart_service.model.Drone;
import com.trader_project.smart_service.model.impl.MyDrone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
public class DroneLauncherImpl implements DroneLauncher {

    private final ExecutorService executorService;
    private final Map<TradeStrategy, Drone> running;


    @Autowired
    public DroneLauncherImpl(@Qualifier("scheduler") ExecutorService executorService) {
        this.executorService = executorService;
        running = new ConcurrentHashMap<>();
    }


    @Override
    public void follow(TradeStrategy strategy, Smart.Account account) {
        Drone drone = running.get(strategy);
        if (drone == null) {
            drone = new MyDrone(strategy);
            drone.run(executorService);
        }
        drone.addSubscriber(account);
    }

    @Override
    public void leave(TradeStrategy strategy, Smart.Account account) {
        Drone drone = running.get(strategy);
        if (drone != null) {
            drone.removeSubscriber(account);
            if (drone.isEmpty()) {
                drone.stop(false);
                running.remove(strategy);
            }
        }
    }
}
