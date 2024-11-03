package com.anastasia.core_service.entity.markets;

import com.anastasia.core_service.entity.enums.Currency;
import com.anastasia.core_service.entity.enums.Board;
import com.anastasia.core_service.entity.enums.ExchangeMarket;
import com.anastasia.core_service.entity.enums.Market;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class Securities implements Serializable, Comparable<Securities> {

    protected String ticker;
    protected String name;
    protected PriceAtTheDate price;
    protected long dayTradeVolume;
    protected Currency currency;
    protected Market market;
    protected Board board;
    protected ExchangeMarket exchangeMarket;


    @Override
    public int compareTo(Securities o) {
        return Long.compare(o.dayTradeVolume, dayTradeVolume);
    }

    public record PriceAtTheDate(double price, LocalDateTime time) implements Serializable {}
}
