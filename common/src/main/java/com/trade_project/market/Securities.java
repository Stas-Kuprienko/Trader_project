package com.trade_project.market;

import com.trade_project.enums.Board;
import com.trade_project.enums.Currency;
import com.trade_project.enums.ExchangeMarket;
import com.trade_project.enums.Market;
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
