package com.anastasia.smart_service.domain.trading.finam_impl;

import com.anastasia.smart_service.Smart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class FinamTradingManager {

    private final ExecutorService executorService;

    @Autowired
    public FinamTradingManager(@Qualifier("virtualExecutorService") ExecutorService executorService) {
        this.executorService = executorService;
    }


    public void deal(Smart.Security security, double price, List<Smart.Account> subscribers) {

    }
}
