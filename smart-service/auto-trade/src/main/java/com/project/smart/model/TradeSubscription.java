package com.project.smart.model;

import com.project.smart.Smart;

public record TradeSubscription(Smart.Security security, Smart.Strategy strategy) {}
