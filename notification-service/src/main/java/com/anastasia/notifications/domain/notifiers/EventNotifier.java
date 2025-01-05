package com.anastasia.notifications.domain.notifiers;

public interface EventNotifier<EVENT> {

    void apply(EVENT event);
}
