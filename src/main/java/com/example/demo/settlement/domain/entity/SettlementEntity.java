package com.example.demo.settlement.domain.entity;

import com.example.demo.creator.domain.entity.CreatorEntity;
import com.example.demo.global.entity.BaseEntity;
import com.example.demo.settlement.domain.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "settlement")
public class SettlementEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", nullable = false)
    private FeeEntity fee;

    @Column(nullable = false)
    private YearMonth settlementMonth;

    @Column(nullable = false)
    private Long totalSales;

    @Column(nullable = false)
    private Long totalRefunds;

    @Column(nullable = false)
    private Long netSales;

    @Column(nullable = false)
    private Long feeAmount;

    @Column(nullable = false)
    private Long expectedPayout;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status;

    @Builder
    public SettlementEntity(CreatorEntity creator, FeeEntity fee, YearMonth settlementMonth,
                            Long totalSales, Long totalRefunds, Long netSales,
                            Long feeAmount, Long expectedPayout) {
        this.creator = creator;
        this.fee = fee;
        this.settlementMonth = settlementMonth;
        this.totalSales = totalSales;
        this.totalRefunds = totalRefunds;
        this.netSales = netSales;
        this.feeAmount = feeAmount;
        this.expectedPayout = expectedPayout;
        this.status = SettlementStatus.PENDING;
    }
}
