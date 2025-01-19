package com.trader_project.smart_service.domain.automation;

import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.strategy.TradeStrategy;

public interface DroneLauncher {

    void follow(TradeStrategy strategy, Smart.Account account);

    void leave(TradeStrategy strategy, Smart.Account account);
}
