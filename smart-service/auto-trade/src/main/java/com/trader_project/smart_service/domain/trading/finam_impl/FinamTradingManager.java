package com.trader_project.smart_service.domain.trading.finam_impl;

import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.event.EventNotificationService;
import com.trader_project.smart_service.model.TradeSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class FinamTradingManager {

    private final EventNotificationService notificationService;
    private final ExecutorService executorService;

    @Autowired
    public FinamTradingManager(EventNotificationService notificationService,
                               @Qualifier("virtualExecutorService") ExecutorService executorService) {
        this.notificationService = notificationService;
        this.executorService = executorService;
    }


    public void deal(TradeSubscription subscription, double price, List<Smart.Account> subscribers) {

    }
}
