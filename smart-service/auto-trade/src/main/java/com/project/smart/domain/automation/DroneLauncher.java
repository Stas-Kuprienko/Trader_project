package com.project.smart.domain.automation;

import com.project.smart.Smart;
import com.project.smart.domain.strategy.TradeStrategy;

public interface DroneLauncher {

    void follow(TradeStrategy strategy, Smart.Account account);

    void leave(TradeStrategy strategy, Smart.Account account);
}
