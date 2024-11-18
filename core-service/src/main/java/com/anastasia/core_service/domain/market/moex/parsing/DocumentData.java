package com.anastasia.core_service.domain.market.moex.parsing;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class DocumentData {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    private Metadata metadata;

    @JacksonXmlElementWrapper(localName = "rows")
    @JacksonXmlProperty(localName = "row")
    private List<Map<String, Object>> rows;


    public DocumentData(String id, Metadata metadata, List<Map<String, Object>> rows) {
        this.id = id;
        this.metadata = metadata;
        this.rows = rows;
    }

    public DocumentData() {}


    public Optional<Map<String, Object>> singleData() {
        if (rows.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(rows.getFirst());
        }
    }
}
