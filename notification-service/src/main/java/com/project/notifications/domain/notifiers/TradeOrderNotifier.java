package com.project.notifications.domain.notifiers;

import com.project.events.TradeOrderEvent;
import com.project.notifications.domain.message.TemplateStore;
import com.project.notifications.entity.Subscriber;
import com.project.notifications.service.EmailService;
import com.project.notifications.service.SubscriberService;
import com.project.notifications.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;

@Notifier(eventType = TradeOrderEvent.class)
public class TradeOrderNotifier implements EventNotifier<TradeOrderEvent> {

    private final SubscriberService subscriberService;
    private final TemplateStore templateStore;
    private final TelegramService telegramService;
    private final EmailService emailService;


    @Autowired
    public TradeOrderNotifier(SubscriberService subscriberService,
                              TemplateStore templateStore,
                              TelegramService telegramService,
                              EmailService emailService) {
        this.subscriberService = subscriberService;
        this.templateStore = templateStore;
        this.telegramService = telegramService;
        this.emailService = emailService;
    }


    @Override
    public void apply(TradeOrderEvent event) {
        Subscriber subscriber = subscriberService.getById(event.getAccountId());
        String template = templateStore.getTemplate(event.getClass(), subscriber.getLanguage());

        if (subscriber.isEmailNotify() && !subscriber.isTelegramNotify()) {
            emailService.sendHtmlEmail(formatting(event, template), subscriber.getEmail());

        } else if (subscriber.isTelegramNotify()) {
            telegramService.sendMessage(formatting(event, template), subscriber.getTelegramId());
        }
    }


    private String formatting(TradeOrderEvent event, String template) {
        return template.formatted(event.toString());
    }
}
