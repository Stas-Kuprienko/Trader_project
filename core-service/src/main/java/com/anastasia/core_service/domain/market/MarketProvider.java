package com.anastasia.core_service.domain.market;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.anastasia.trade_project.enums.ExchangeMarket;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MarketProvider {

    @AliasFor(annotation = Component.class)
    String value() default "";

    ExchangeMarket exchange();
}
