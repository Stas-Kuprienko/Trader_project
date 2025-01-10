package com.anastasia.core_service.domain.smart;

import com.anastasia.trade_project.enums.TradeScope;
import com.anastasia.trade_project.market.Securities;

public record TradeSubscription(Securities securities, String strategy, TradeScope tradeScope) {}
