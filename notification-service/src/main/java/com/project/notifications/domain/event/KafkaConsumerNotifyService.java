package com.project.notifications.domain.event;

import com.project.notifications.domain.notifiers.EventNotifier;
import com.project.notifications.domain.notifiers.Notifier;
import com.project.events.NotifySubscriptionEvent;
import com.project.events.TradeSubscriptionEvent;
import com.project.events.TradeOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class KafkaConsumerNotifyService {

    private final ConcurrentHashMap<String, EventNotifier<Object>> notifiers;

    @Autowired
    public KafkaConsumerNotifyService(ConfigurableApplicationContext applicationContext) {
        notifiers = collectNotifiers(applicationContext);
    }


    @KafkaListener(topics = "notify-subscription-topic")
    public void handle(NotifySubscriptionEvent notifySubscription) {
        log.info("Message is received: " + notifySubscription);
        EventNotifier<Object> notifier = notifiers.get(notifySubscription.getClass().getSimpleName());
        if (notifier != null) {
            notifier.apply(notifySubscription);
        }
    }

    @KafkaListener(topics = "trade-subscription-topic")
    public void handle(TradeSubscriptionEvent tradeSubscription) {
        log.info("Message is received: " + tradeSubscription);
        EventNotifier<Object> notifier = notifiers.get(tradeSubscription.getClass().getSimpleName());
        if (notifier != null) {
            notifier.apply(tradeSubscription);
        }
    }

    @KafkaListener(topics = "trade-order-topic")
    public void handle(TradeOrderEvent tradeOrder) {
        log.info("Message is received: " + tradeOrder);
        EventNotifier<Object> notifier = notifiers.get(tradeOrder.getClass().getSimpleName());
        if (notifier != null) {
            notifier.apply(tradeOrder);
        }
    }


    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, EventNotifier<Object>> collectNotifiers(ConfigurableApplicationContext context) {
        ConcurrentHashMap<String, EventNotifier<Object>> map = new ConcurrentHashMap<>();
        context.getBeansOfType(EventNotifier.class)
                .forEach((k, v) -> {
                    Notifier notifier = v.getClass().getAnnotation(Notifier.class);
                    if (notifier != null) {
                        String eventType = notifier.eventType().getSimpleName();
                        map.put(eventType, v);
                        log.info("Notifier for {} is registered", eventType);
                    }
                });
        return map;
    }
}
