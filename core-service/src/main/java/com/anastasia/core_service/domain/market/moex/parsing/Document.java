package com.anastasia.core_service.domain.market.moex.parsing;

import com.anastasia.core_service.exception.InternalServiceException;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class Document {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "data")
    private List<DocumentData> data;


    public Document(List<DocumentData> data) {
        this.data = data;
    }

    public Document() {}


    public List<Map<String, Object>> securitiesData() {
        for (var d : data) {
            if (d.getId().equals(ID.securities.name())) {
                return d.getRows();
            }
        }
        throw InternalServiceException.unexpectedData(ExchangeMarket.MOEX.name(), data);
    }

    public List<Map<String, Object>> marketData() {
        for (var d : data) {
            if (d.getId().equals(ID.marketdata.name())) {
                return d.getRows();
            }
        }
        throw InternalServiceException.unexpectedData(ExchangeMarket.MOEX.name(), data);
    }

    public Optional<Map<String, Object>> singleSecuritiesData() {
        for (var d : data) {
            if (d.getId().equals(ID.securities.name())) {
                return d.singleData();
            }
        }
        return Optional.empty();
    }

    public Optional<Map<String, Object>> singleMarketData() {
        for (var d : data) {
            if (d.getId().equals(ID.marketdata.name())) {
                return d.singleData();
            }
        }
        return Optional.empty();
    }


    enum ID {
        securities,
        marketdata
    }
}
