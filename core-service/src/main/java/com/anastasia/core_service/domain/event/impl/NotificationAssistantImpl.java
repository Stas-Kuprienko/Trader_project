package com.anastasia.core_service.domain.event.impl;

import com.anastasia.core_service.configuration.CoreServiceConfig;
import com.anastasia.core_service.domain.event.MessageService;
import com.anastasia.core_service.domain.event.NotificationAssistant;
import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Broker;
import com.anastasia.trade_project.enums.Direction;
import com.anastasia.trade_project.enums.TradeScope;
import com.anastasia.trade_project.events.SubscriptionStatus;
import com.anastasia.trade_project.events.TradeNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class NotificationAssistantImpl implements NotificationAssistant {

    private final MessageService<TradeNotification> tradeNotificationMessageService;
    private final MessageService<SubscriptionStatus> subscribeStatusMessageService;

    @Autowired
    public NotificationAssistantImpl(@Qualifier("messageServiceTradeNotification") MessageService<TradeNotification> tradeNotificationMessageService,
                                     @Qualifier("messageServiceSubscribeStatus") MessageService<SubscriptionStatus> subscribeStatusMessageService) {
        this.tradeNotificationMessageService = tradeNotificationMessageService;
        this.subscribeStatusMessageService = subscribeStatusMessageService;
    }


    @Override
    public CompletableFuture<?> direct(Smart.OrderNotification notification) {
        return CompletableFuture.runAsync(() -> notification
                .getOrderListList()
                .stream()
                .map(this::convert)
                .forEach(tradeNotification -> tradeNotificationMessageService
                        .send(tradeNotification)
                        .subscribe(sendResult -> log.info(sendResult.toString()))));
    }

    @Override
    public CompletableFuture<?> direct(Smart.StatusResponse status, SubscriptionStatus.Option option) {
        return CompletableFuture.runAsync(() -> subscribeStatusMessageService
                .send(convert(status, option))
                .subscribe(sendResult -> log.info(sendResult.toString())));
    }


    private TradeNotification convert(Smart.Order order) {
        return TradeNotification.builder()
                .transactionId(order.getTransactionId())
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

    private SubscriptionStatus convert(Smart.StatusResponse status, SubscriptionStatus.Option option) {
        return SubscriptionStatus.builder()
                .tradeStrategy(status.getStrategy().getName())
                .tradeScope(TradeScope.valueOf(status.getStrategy().getTradeScope().name()))
                .option(option)
                .build();
    }
}
