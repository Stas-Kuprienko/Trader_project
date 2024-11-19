package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.configuration.CoreServiceConfig;
import com.anastasia.core_service.domain.market.moex.parsing.Document;
import com.anastasia.core_service.domain.market.moex.parsing.StockMarketColumns;
import com.anastasia.core_service.domain.market.moex.parsing.StockSecuritiesColumns;
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
        String stringPrice = (String) marketDataRow.get(StockMarketColumns.LAST.name());
        String stringDateTime = getNotNullValue(marketDataRow, StockMarketColumns.SYSTIME.name(), String.class);

        return Stock.builder()
                .ticker(getNotNullValue(securitiesRow, StockSecuritiesColumns.SECID.name(), String.class))
                .name(getNotNullValue(securitiesRow, StockSecuritiesColumns.SECNAME.name(), String.class))
                .currency(currencyValue(getNotNullValue(securitiesRow, StockSecuritiesColumns.CURRENCYID.name(), String.class)))
                .price(priceAtTheDateValue(stringPrice, stringDateTime))
                .lotSize(getNotNullValue(securitiesRow, StockSecuritiesColumns.LOTSIZE.name(), Integer.class))
                .dayTradeVolume(getNotNullValue(marketDataRow, StockMarketColumns.VALTODAY.name(), Long.class))
                .market(Market.Stock)
                .board(Board.valueOf(StockSecuritiesColumns.BOARDID.name()))
                .exchangeMarket(ExchangeMarket.MOEX)
                .build();
    }

    public Futures futures(Map<String, String> row) {
        return Futures.builder()
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

    private Securities.PriceAtTheDate priceAtTheDateValue(String price, String dateTime) {
        double doublePrice = Double.parseDouble(price != null ? price : "0.0");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, CoreServiceConfig.DATE_TIME_FORMAT);
        return new Securities.PriceAtTheDate(doublePrice, localDateTime);
    }
}
