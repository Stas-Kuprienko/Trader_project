package com.project.core.domain.event.impl;

import com.project.core.configuration.CoreServiceConfig;
import com.project.core.domain.event.MessageService;
import com.project.core.domain.event.NotificationAssistant;
import com.project.enums.Board;
import com.project.enums.Broker;
import com.project.enums.Direction;
import com.project.enums.TradeScope;
import com.project.events.NotifySubscriptionEvent;
import com.project.events.TradeSubscriptionEvent;
import com.project.events.TradeOrderEvent;
import com.project.models.StrategyDefinition;
import com.project.smart.Smart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class NotificationAssistantImpl implements NotificationAssistant {

    private final MessageService<TradeOrderEvent> tradeOrderMessageService;
    private final MessageService<TradeSubscriptionEvent> tradeSubscriptionMessageService;
    private final MessageService<NotifySubscriptionEvent> notifySubscriptionMessageService;

    @Autowired
    public NotificationAssistantImpl(@Qualifier("tradeOrderMessageService") MessageService<TradeOrderEvent> tradeOrderMessageService,
                                     @Qualifier("tradeSubscriptionMessageService") MessageService<TradeSubscriptionEvent> tradeSubscriptionMessageService,
                                     @Qualifier("notifySubscriptionMessageService") MessageService<NotifySubscriptionEvent> notifySubscriptionMessageService) {
        this.tradeOrderMessageService = tradeOrderMessageService;
        this.tradeSubscriptionMessageService = tradeSubscriptionMessageService;
        this.notifySubscriptionMessageService = notifySubscriptionMessageService;
    }


    @Override
    public CompletableFuture<?> direct(Smart.OrderNotification notification) {
        return CompletableFuture.runAsync(() -> notification
                .getOrderListList()
                .stream()
                .map(this::convert)
                .forEach(tradeNotification -> tradeOrderMessageService
                        .send(tradeNotification)
                        .subscribe(sendResult -> log.info(sendResult.toString()))));
    }

    @Override
    public CompletableFuture<?> direct(Smart.StatusResponse status, TradeSubscriptionEvent.Option option) {
        return CompletableFuture.runAsync(() -> tradeSubscriptionMessageService
                .send(convert(status, option))
                .subscribe(sendResult -> log.info(sendResult.toString())));
    }

    public CompletableFuture<?> direct() {
        //TODO
        return CompletableFuture.runAsync(() -> notifySubscriptionMessageService
                .send(null)
                .subscribe(sendResult -> log.info(sendResult.toString())));
    }


    private TradeOrderEvent convert(Smart.Order order) {
        return TradeOrderEvent.builder()
                .transactionId(order.getTransactionId())
                .userId(UUID.fromString(order.getAccount().getUserId()))
                .broker(Broker.valueOf(order.getAccount().getBroker()))
                .clientId(order.getAccount().getClientId())
                .ticker(order.getSecurity().getTicker())
                .board(Board.valueOf(order.getSecurity().getBoard()))
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .direction(Direction.parse(order.getDirection()))
                .time(LocalDateTime.parse(order.getTime(), CoreServiceConfig.DATE_TIME_FORMAT))
                .build();
    }

    private TradeSubscriptionEvent convert(Smart.StatusResponse status, TradeSubscriptionEvent.Option option) {
        StrategyDefinition strategyDefinition = StrategyDefinition.builder()
                .name(status.getStrategy().getName())
                .tradeScope(TradeScope.valueOf(status.getStrategy().getTradeScope().name()))
                .build();
        return TradeSubscriptionEvent.builder()
                .broker(Broker.valueOf(status.getAccount().getBroker()))
                .clientId(status.getAccount().getClientId())
                .userId(UUID.fromString(status.getAccount().getUserId()))
                .ticker(status.getSecurity().getTicker())
                .board(Board.valueOf(status.getSecurity().getBoard()))
                .strategyDefinition(strategyDefinition)
                .option(option)
                .success(status.getSuccess())
                .time(LocalDateTime.now())
                .build();
    }
}
