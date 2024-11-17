package com.anastasia.core_service.domain.market.moex.parsing;

import com.anastasia.core_service.exception.IncorrectContentException;
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


    public List<Map<String, String>> securitiesData() {
        for (var d : data) {
            if (d.getId().equals(ID.securities.name())) {
                return d.getRows();
            }
        }
        throw new IncorrectContentException("Unexpected data of XML from 'MOEX': " + data);
    }

    public List<Map<String, String>> marketData() {
        for (var d : data) {
            if (d.getId().equals(ID.marketdata.name())) {
                return d.getRows();
            }
        }
        throw new IncorrectContentException("Unexpected data of XML from 'MOEX': " + data);
    }

    public Optional<Map<String, String>> singleSecuritiesData() {
        for (var d : data) {
            if (d.getId().equals(ID.securities.name())) {
                return d.singleData();
            }
        }
        return Optional.empty();
    }

    public Optional<Map<String, String>> singleMarketData() {
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
