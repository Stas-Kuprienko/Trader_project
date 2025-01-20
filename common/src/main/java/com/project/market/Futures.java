package com.project.market;

import com.project.enums.Board;
import com.project.enums.Currency;
import com.project.enums.ExchangeMarket;
import com.project.enums.Market;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import java.time.LocalDate;
import java.util.Objects;

@Data
@RedisHash("futures")
public class Futures extends Securities {

    private String asset;
    private double minStep;
    private double stepPrice;
    private LocalDate expiration;


    @Builder
    public Futures(String ticker,
                   String name,
                   String asset,
                   double minStep,
                   double stepPrice,
                   Currency currency,
                   PriceAtTheDate price,
                   long dayTradeVolume,
                   LocalDate expiration,
                   Market market,
                   Board board,
                   ExchangeMarket exchangeMarket) {
        this.ticker = ticker;
        this.name = name;
        this.asset = asset;
        this.minStep = minStep;
        this.stepPrice = stepPrice;
        this.currency = currency;
        this.price = price;
        this.dayTradeVolume = dayTradeVolume;
        this.expiration = expiration;
        this.market = market;
        this.board = board;
        this.exchange = exchangeMarket;
    }

    public Futures() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Futures futures)) return false;
        return Objects.equals(ticker, futures.ticker) &&
                Objects.equals(name, futures.name) &&
                Objects.equals(asset, futures.asset) &&
                currency == futures.currency &&
                Objects.equals(expiration, futures.expiration) &&
                market == futures.market &&
                board == futures.board;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, name, asset, currency, market, board);
    }

    @Override
    public String toString() {
        return "Futures{" +
                "ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", asset='" + asset + '\'' +
                ", minStep=" + minStep +
                ", stepPrice=" + stepPrice +
                ", currency=" + currency +
                ", price=" + price +
                ", expiration=" + expiration +
                ", market=" + market +
                ", board=" + board +
                ", exchangeMarket=" + exchange +
                '}';
    }
}
