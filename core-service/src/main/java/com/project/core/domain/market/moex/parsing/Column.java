package com.project.core.domain.market.moex.parsing;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Column {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String bytes;

    @JacksonXmlProperty(isAttribute = true)
    private String max_size;


    public Column(String name, String type, String bytes, String max_size) {
        this.name = name;
        this.type = type;
        this.bytes = bytes;
        this.max_size = max_size;
    }

    public Column() {}
}
