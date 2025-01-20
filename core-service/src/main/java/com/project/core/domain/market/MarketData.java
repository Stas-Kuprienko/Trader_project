package com.project.core.domain.market;

import com.project.enums.ExchangeMarket;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MarketData {

    @AliasFor(annotation = Component.class)
    String value() default "";

    ExchangeMarket exchange();
}
