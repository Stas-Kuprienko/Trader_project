package com.anastasia.core_service.domain.event;

import com.anastasia.core_service.configuration.CoreServiceConfig;
import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Broker;
import com.anastasia.trade_project.enums.Direction;
import com.anastasia.trade_project.notification.TradeNotification;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class NotificationAssistant {

    private final MessageService<TradeNotification> messageService;

    @Autowired
    public NotificationAssistant(MessageService<TradeNotification> messageService) {
        this.messageService = messageService;
    }


    public CompletableFuture<?> handle(Smart.OrderNotification notification) {
        return CompletableFuture.runAsync(() -> {
            //TODO
            notification.getOrderListList().stream().map(this::convert).forEach(messageService::send);
        });
    }

    public CompletableFuture<?> sendResponse(Smart.StatusResponse status) {
        return CompletableFuture.runAsync(() -> {

        });
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
}
