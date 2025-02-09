package com.project.notifications.domain.message;

import com.project.enums.Language;

public interface MessageDispatcher {
    String pickUp(Class<?> eventClass, Language language);
}
