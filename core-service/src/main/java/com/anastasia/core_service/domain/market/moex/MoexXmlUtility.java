package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.domain.market.moex.parsing.Document;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    public Stock stock(Map<String, String> row) {
        return Stock.builder()
                .build();
    }

    public Futures futures(Map<String, String> row) {
        return Futures.builder()
                .build();
    }
}
