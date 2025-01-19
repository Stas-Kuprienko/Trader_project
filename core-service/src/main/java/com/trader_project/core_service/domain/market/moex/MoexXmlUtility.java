package com.trader_project.core_service.domain.market.moex;

import com.trader_project.core_service.configuration.CoreServiceConfig;
import com.trader_project.core_service.domain.market.moex.parsing.*;
import com.trader_project.core_service.exception.InternalServiceException;
import com.trade_project.enums.Board;
import com.trade_project.enums.Currency;
import com.trade_project.enums.ExchangeMarket;
import com.trade_project.enums.Market;
import com.trade_project.market.Futures;
import com.trade_project.market.Securities;
import com.trade_project.market.Stock;
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
        String stringCurrency = getNotNullValue(securitiesRow, StockSecuritiesColumns.CURRENCYID.name());
        return Stock.builder()
                .ticker(getNotNullValue(securitiesRow, StockSecuritiesColumns.SECID.name()))
                .name(getNotNullValue(securitiesRow, StockSecuritiesColumns.SECNAME.name()))
                .currency(currencyValue(stringCurrency))
                .price(priceAtTheDate(marketDataRow))
                .lotSize(Integer.parseInt(getNotNullValue(securitiesRow, StockSecuritiesColumns.LOTSIZE.name())))
                .dayTradeVolume(Long.parseLong(getNotNullValue(marketDataRow, StockMarketColumns.VALTODAY.name())))
                .market(Market.Stock)
                .board(Board.valueOf(getNotNullValue(securitiesRow, StockSecuritiesColumns.BOARDID.name())))
                .exchangeMarket(ExchangeMarket.MOEX)
                .build();
    }

    public Futures futures(Map<String, Object> securitiesRow, Map<String, Object> marketDataRow) {
        LocalDate expiration = LocalDate
                .parse(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.LASTDELDATE.name()));
        return Futures.builder()
                .ticker(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.SECID.name()))
                .name(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.SHORTNAME.name()))
                .asset(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.ASSETCODE.name()))
                .minStep(Double.parseDouble(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.MINSTEP.name())))
                .stepPrice(Double.parseDouble(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.STEPPRICE.name())))
                .price(priceAtTheDate(marketDataRow))
                .currency(Currency.RUR)
                .dayTradeVolume(Long.parseLong(getNotNullValue(marketDataRow, FuturesMarketColumns.VALTODAY.name())))
                .expiration(expiration)
                .board(Board.valueOf(getNotNullValue(securitiesRow, FuturesSecuritiesColumns.BOARDID.name())))
                .market(Market.Futures)
                .exchangeMarket(ExchangeMarket.MOEX)
                .build();
    }


    private String getNotNullValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null) {
            throw InternalServiceException.unexpectedData(ExchangeMarket.MOEX.name(), row);
        } else {
            return (String) value;
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
        double doublePrice = stringPrice == null || stringPrice.isEmpty() ? 0.0 : Double.parseDouble(stringPrice);

        String stringDateTime = getNotNullValue(marketData, StockMarketColumns.SYSTIME.name());
        LocalDateTime localDateTime = LocalDateTime.parse(stringDateTime, CoreServiceConfig.DATE_TIME_FORMAT);

        return new Securities.PriceAtTheDate(doublePrice, localDateTime);
    }
}
