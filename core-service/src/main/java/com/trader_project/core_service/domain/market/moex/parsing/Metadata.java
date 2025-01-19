package com.trader_project.core_service.domain.market.moex.parsing;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import java.util.List;

@Data
public class Metadata {

    @JacksonXmlElementWrapper(localName = "columns")
    @JacksonXmlProperty(localName = "column")
    private List<Column> columns;


    public Metadata(List<Column> columns) {
        this.columns = columns;
    }

    public Metadata() {}
}
