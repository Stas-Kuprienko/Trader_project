package com.anastasia.notifications.domain.notifiers;

import com.anastasia.notifications.domain.message.TemplateStore;
import com.anastasia.notifications.service.EmailService;
import com.anastasia.notifications.service.TelegramService;
import com.anastasia.trade_project.events.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;

@Notifier(eventType = SubscriptionStatus.class)
public class SubscriptionNotifier implements EventNotifier<SubscriptionStatus> {

    private final TemplateStore templateStore;
    private final TelegramService telegramService;
    private final EmailService emailService;


    @Autowired
    public SubscriptionNotifier(TemplateStore templateStore,
                                TelegramService telegramService,
                                EmailService emailService) {
        this.templateStore = templateStore;
        this.telegramService = telegramService;
        this.emailService = emailService;
    }


    @Override
    public void apply(SubscriptionStatus subscriptionStatus) {
        //TODO just for test
        String key = subscriptionStatus.getClass().getSimpleName();
        key += '_' + "EN";
        String template = templateStore.getTemplate(key);
        emailService.sendHtmlEmail(template, "?");
    }


}
