package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Broker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PortfolioDto {

    @JsonProperty("client_id")
    private String clientId;

    private Broker broker;

    private double balance;

    private List<PositionDto> positions;

    @JsonProperty("relevance_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime relevanceAt;


    @Builder
    public PortfolioDto(String clientId, Broker broker, double balance, List<PositionDto> positions) {
        this.clientId = clientId;
        this.broker = broker;
        this.balance = balance;
        this.positions = positions;
    }

    public PortfolioDto() {}
}
