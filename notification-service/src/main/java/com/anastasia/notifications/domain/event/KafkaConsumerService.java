package com.anastasia.notifications.domain.event;

import com.anastasia.notifications.domain.notifiers.EventNotifier;
import com.anastasia.notifications.domain.notifiers.Notifier;
import com.anastasia.trade_project.events.SubscriptionStatus;
import com.anastasia.trade_project.events.TradeNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class KafkaConsumerService {

    private final ConcurrentHashMap<String, EventNotifier<Object>> notifiers;

    @Autowired
    public KafkaConsumerService(ConfigurableApplicationContext applicationContext) {
        notifiers = collectNotifiers(applicationContext);
    }


    @KafkaListener(topics = "subscribe-status-topic")
    public void handle(SubscriptionStatus subscription) {
        log.info("Message is received: " + subscription);
        EventNotifier<Object> notifier = notifiers.get(subscription.getClass().getSimpleName());
        if (notifier != null) {
            notifier.apply(subscription);
        }
    }

    @KafkaListener(topics = "trade-notification-topic")
    public void handle(TradeNotification tradeNotification) {
        log.info("Message is received: " + tradeNotification);
        EventNotifier<Object> notifier = notifiers.get(tradeNotification.getClass().getSimpleName());
        if (notifier != null) {
            notifier.apply(tradeNotification);
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
