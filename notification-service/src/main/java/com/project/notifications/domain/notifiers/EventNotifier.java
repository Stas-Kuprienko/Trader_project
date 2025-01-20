package com.project.notifications.domain.notifiers;

public interface EventNotifier<EVENT> {

    void apply(EVENT event);
}
