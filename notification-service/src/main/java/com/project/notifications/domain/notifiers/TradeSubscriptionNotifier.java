package com.project.notifications.domain.notifiers;

import com.project.events.TradeSubscriptionEvent;
import com.project.notifications.domain.message.TemplateStore;
import com.project.notifications.entity.Subscriber;
import com.project.notifications.service.EmailService;
import com.project.notifications.service.SubscriberService;
import com.project.notifications.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;

@Notifier(eventType = TradeSubscriptionEvent.class)
public class TradeSubscriptionNotifier implements EventNotifier<TradeSubscriptionEvent> {

    private final SubscriberService subscriberService;
    private final TemplateStore templateStore;
    private final TelegramService telegramService;
    private final EmailService emailService;


    @Autowired
    public TradeSubscriptionNotifier(SubscriberService subscriberService,
                                     TemplateStore templateStore,
                                     TelegramService telegramService,
                                     EmailService emailService) {
        this.subscriberService = subscriberService;
        this.templateStore = templateStore;
        this.telegramService = telegramService;
        this.emailService = emailService;
    }


    @Override
    public void apply(TradeSubscriptionEvent event) {
        Subscriber subscriber = subscriberService.getById(event.getAccountId());

        if (subscriber.isEmailNotify() && !subscriber.isTelegramNotify()) {
            String template = templateStore
                    .getTemplate(event.getClass(), subscriber.getLanguage(), TemplateStore.Method.EMAIL);
            emailService.sendHtmlEmail(formatting(event, template), subscriber.getEmail());

        } else if (subscriber.isTelegramNotify()) {
            String template = templateStore
                    .getTemplate(event.getClass(), subscriber.getLanguage(), TemplateStore.Method.TELEGRAM);
            telegramService.sendMessage(formatting(event, template), subscriber.getTelegramId());
        }
    }


    private String formatting(TradeSubscriptionEvent event, String template) {
        //TODO
        return template.formatted(event.toString());
    }
}
