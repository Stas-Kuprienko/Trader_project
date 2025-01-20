package com.project.core.domain.smart;

import com.project.enums.TradeScope;
import com.project.market.Securities;

public record TradeSubscription(Securities securities, String strategy, TradeScope tradeScope) {}
