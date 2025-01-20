package com.project.market;

import com.project.enums.Board;
import com.project.enums.Currency;
import com.project.enums.ExchangeMarket;
import com.project.enums.Market;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public abstract class Securities implements Serializable, Comparable<Securities> {

    protected String ticker;
    protected String name;
    protected PriceAtTheDate price;
    protected long dayTradeVolume;
    protected Currency currency;
    protected Market market;
    protected Board board;
    protected ExchangeMarket exchange;


    @Override
    public int compareTo(Securities o) {
        return Long.compare(o.dayTradeVolume, dayTradeVolume);
    }

    public record PriceAtTheDate(double price, LocalDateTime time) implements Serializable {}
}
