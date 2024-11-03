package com.anastasia.core_service.entity.user;

import com.anastasia.core_service.entity.enums.Broker;
import com.anastasia.core_service.entity.enums.Market;
import com.anastasia.core_service.entity.enums.Currency;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Portfolio {

    private String clientId;
    private Broker broker;
    private BigDecimal balance;
    private List<Position> positions;

    @Builder
    public Portfolio(String clientId, Broker broker, BigDecimal balance, List<Position> positions) {
        this.clientId = clientId;
        this.broker = broker;
        this.balance = balance;
        this.positions = positions;
    }

    public Portfolio() {
        positions = new ArrayList<>();
    }


    public record Position(String ticker,
                           Market market,
                           double price,
                           double totalCost,
                           long quantity,
                           Currency currency,
                           double profit) {}
}
