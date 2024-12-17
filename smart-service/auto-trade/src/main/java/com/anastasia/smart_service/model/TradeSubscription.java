package com.anastasia.smart_service.model;

import com.anastasia.smart_service.Smart;

public record TradeSubscription(Smart.Security security, Smart.Strategy strategy) {}
