package com.anastasia.trade_project.markets;

import com.anastasia.trade_project.enums.Currency;
import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Market;
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
