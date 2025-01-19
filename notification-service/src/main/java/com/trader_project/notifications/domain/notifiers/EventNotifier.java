package com.trader_project.notifications.domain.notifiers;

public interface EventNotifier<EVENT> {

    void apply(EVENT event);
}
