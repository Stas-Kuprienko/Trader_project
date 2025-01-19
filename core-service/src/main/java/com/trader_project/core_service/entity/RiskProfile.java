package com.trader_project.core_service.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Table(name = "risk_profile", schema = "person")
public class RiskProfile {

    @Id
    private UUID id;

    @Column("deal_loss_percentage")
    private byte dealLossPercentage;

    @Column("account_loss_percentage")
    private byte accountLossPercentage;

    @Column("futures_in_account_percentage")
    private byte futuresInAccountPercentage;

    @Column("stocks_in_account_percentage")
    private byte stockInAccountPercentage;

    @Column("risk_type")
    private RiskType riskType;

    @Column("created_at")
    private LocalDate createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public RiskProfile(UUID id,
                       byte dealLossPercentage,
                       byte accountLossPercentage,
                       byte futuresInAccountPercentage,
                       byte stockInAccountPercentage,
                       RiskType riskType,
                       LocalDate createdAt,
                       LocalDateTime updatedAt) {
        this.id = id;
        this.dealLossPercentage = dealLossPercentage;
        this.accountLossPercentage = accountLossPercentage;
        this.futuresInAccountPercentage = futuresInAccountPercentage;
        this.stockInAccountPercentage = stockInAccountPercentage;
        this.riskType = riskType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public RiskProfile() {}


    public enum RiskType {

        conservative,
        medium,
        aggressive

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RiskProfile that)) return false;
        return dealLossPercentage == that.dealLossPercentage &&
                accountLossPercentage == that.accountLossPercentage &&
                futuresInAccountPercentage == that.futuresInAccountPercentage &&
                stockInAccountPercentage == that.stockInAccountPercentage &&
                riskType == that.riskType &&
                createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dealLossPercentage,
                accountLossPercentage,
                futuresInAccountPercentage,
                stockInAccountPercentage,
                riskType, createdAt);
    }

    @Override
    public String toString() {
        return "RiskProfile{" +
                "id=" + id +
                ", dealLossPercentage=" + dealLossPercentage +
                ", accountLossPercentage=" + accountLossPercentage +
                ", futuresInAccountPercentage=" + futuresInAccountPercentage +
                ", StockInAccountPercentage=" + stockInAccountPercentage +
                ", riskType=" + riskType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
