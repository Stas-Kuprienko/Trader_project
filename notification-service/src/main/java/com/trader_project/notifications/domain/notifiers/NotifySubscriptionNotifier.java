package com.trader_project.notifications.domain.notifiers;

import com.trader_project.notifications.domain.message.TemplateStore;
import com.trader_project.notifications.service.EmailService;
import com.trader_project.notifications.service.TelegramService;
import com.trade_project.events.NotifySubscriptionEvent;
import org.springframework.beans.factory.annotation.Autowired;

@Notifier(eventType = NotifySubscriptionEvent.class)
public class NotifySubscriptionNotifier implements EventNotifier<NotifySubscriptionEvent> {

    private final TemplateStore templateStore;
    private final TelegramService telegramService;
    private final EmailService emailService;


    @Autowired
    public NotifySubscriptionNotifier(TemplateStore templateStore,
                                      TelegramService telegramService,
                                      EmailService emailService) {
        this.templateStore = templateStore;
        this.telegramService = telegramService;
        this.emailService = emailService;
    }


    @Override
    public void apply(NotifySubscriptionEvent notifySubscriptionEvent) {

    }


}
