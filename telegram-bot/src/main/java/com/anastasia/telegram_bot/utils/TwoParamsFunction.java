package com.anastasia.telegram_bot.utils;

@FunctionalInterface
public interface TwoParamsFunction <P1, P2, R> {

    R apply(P1 p1, P2 p2);
}
