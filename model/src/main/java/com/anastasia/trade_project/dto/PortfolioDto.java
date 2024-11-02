package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class PortfolioDto {

    @JsonProperty("client_id")
    private String clientId;

    private String broker;

    private double balance;

    private List<PositionDto> positions;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3}) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$")
    @JsonProperty("relevance_at")
    private String relevanceAt;


    @Builder
    public PortfolioDto(String clientId, String broker, double balance, List<PositionDto> positions) {
        this.clientId = clientId;
        this.broker = broker;
        this.balance = balance;
        this.positions = positions;
    }

    public PortfolioDto() {}
}
