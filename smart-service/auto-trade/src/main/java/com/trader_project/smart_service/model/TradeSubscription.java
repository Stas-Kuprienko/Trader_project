package com.trader_project.smart_service.model;

import com.trader_project.smart_service.Smart;

public record TradeSubscription(Smart.Security security, Smart.Strategy strategy) {}
