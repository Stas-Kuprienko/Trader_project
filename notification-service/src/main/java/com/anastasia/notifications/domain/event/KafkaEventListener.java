package com.anastasia.notifications.domain.event;

import com.anastasia.notifications.domain.notifiers.EventNotifier;
import com.anastasia.notifications.domain.notifiers.Notifier;
import com.anastasia.trade_project.events.SubscriptionStatus;
import com.anastasia.trade_project.events.TradeNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import java.util.concurrent.ConcurrentHashMap;

@KafkaListener
public class KafkaEventListener {

    private final ConcurrentHashMap<String, EventNotifier<Object>> notifiers;

    @Autowired
    public KafkaEventListener(ConfigurableApplicationContext applicationContext) {
        notifiers = collectNotifiers(applicationContext);
    }


    @KafkaHandler
    public void listen(SubscriptionStatus subscription) {
        EventNotifier<Object> notifier = notifiers.get(subscription.getClass().getSimpleName());
        if (notifier != null) {
            notifier.apply(subscription);
        }
    }

    @KafkaHandler
    public void listen(TradeNotification tradeNotification) {
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
                        map.put(notifier.eventType().getSimpleName(), v);
                    }
                });
        return map;
    }
}
