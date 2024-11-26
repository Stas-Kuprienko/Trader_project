package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.configuration.CoreServiceConfig;
import com.anastasia.core_service.domain.market.moex.parsing.*;
import com.anastasia.core_service.exception.IncorrectContentException;
import com.anastasia.trade_project.enums.Board;
import com.anastasia.trade_project.enums.Currency;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Market;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Securities;
import com.anastasia.trade_project.markets.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class MoexXmlUtility {

    private final XmlMapper xmlMapper;


    @Autowired
    public MoexXmlUtility(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }


    public Document parse(String xml) {
        try {
            return xmlMapper.readValue(xml, Document.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Stock stock(Map<String, Object> securitiesRow, Map<String, Object> marketDataRow) {
        String stringCurrency = getNotNullValue(securitiesRow, StockSecuritiesColumns.CURRENCYID.name(), String.class);
        return Stock.builder()
                .ticker(getNotNullValue(securitiesRow, StockSecuritiesColumns.SECID.name(), String.class))
                .name(getNotNullValue(securitiesRow, StockSecuritiesColumns.SECNAME.name(), String.class))
                .currency(currencyValue(stringCurrency))
                .price(priceAtTheDate(marketDataRow))
                .lotSize(getNotNullValue(securitiesRow, StockSecuritiesColumns.LOTSIZE.name(), Integer.class))
                .dayTradeVolume(getNotNullValue(marketDataRow, StockMarketColumns.VALTODAY.name(), Long.class))
                .market(Market.Stock)
                .board(Board.valueOf(getNotNullValue(securitiesRow, StockSecuritiesColumns.BOARDID.name(), String.class)))
                .exchangeMarket(ExchangeMarket.MOEX)
                .build();
    }

    public Futures futures(Map<String, Object> securitiesRow, Map<String, Object> marketDataRow) {
        LocalDate expiration = LocalDate
                .parse(getNotNullValue(marketDataRow, FuturesSecuritiesColumns.LASTDELDATE.name(), String.class));
        return Futures.builder()
                .ticker(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.SECID.name(), String.class))
                .name(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.SHORTNAME.name(), String.class))
                .asset(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.ASSETCODE.name(), String.class))
                .minStep(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.MINSTEP.name(), Double.class))
                .stepPrice(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.STEPPRICE.name(), Double.class))
                .price(priceAtTheDate(marketDataRow))
                .currency(Currency.RUR)
                .dayTradeVolume(getNotNullValue(marketDataRow, FuturesMarketColumns.VALTODAY.name(), Long.class))
                .expiration(expiration)
                .board(Board.valueOf(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.BOARDID.name(), String.class)))
                .market(Market.Forts)
                .exchangeMarket(ExchangeMarket.MOEX)
                .build();
    }


    private <E> E getNotNullValue(Map<String, Object> row, String key, Class<E> clas) {
        Object value = row.get(key);
        if (value == null) {
            throw new IncorrectContentException("Unexpected data of XML from 'MOEX': " + row);
        } else {
            return clas.cast(value);
        }
    }

    private Currency currencyValue(String arg) {
        if (arg != null && arg.equals("SUR")) {
            return Currency.RUR;
        } else {
            return Currency.valueOf(arg);
        }
    }

    private Securities.PriceAtTheDate priceAtTheDate(Map<String, Object> marketData) {
        String stringPrice = (String) marketData.get(StockMarketColumns.LAST.name());
        double doublePrice = stringPrice != null ? Double.parseDouble(stringPrice) : 0.0;

        String stringDateTime = getNotNullValue(marketData, StockMarketColumns.SYSTIME.name(), String.class);
        LocalDateTime localDateTime = LocalDateTime.parse(stringDateTime, CoreServiceConfig.DATE_TIME_FORMAT);

        return new Securities.PriceAtTheDate(doublePrice, localDateTime);
    }
}
