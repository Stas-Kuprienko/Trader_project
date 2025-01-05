package com.anastasia.notifications.domain.notifiers;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Notifier {

    @AliasFor(annotation = Component.class)
    String value() default "";

    Class<?> eventType();
}
