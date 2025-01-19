package com.trader_project.core_service.domain.smart;

import com.trade_project.enums.TradeScope;
import com.trade_project.market.Securities;

public record TradeSubscription(Securities securities, String strategy, TradeScope tradeScope) {}
