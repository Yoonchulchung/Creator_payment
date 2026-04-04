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
@Table(name = "settlement", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"creator_id", "settlement_month"}) // 동일 기간 중복 정산 방지 로직. 
})
public class SettlementEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", nullable = false)
    private FeeEntity fee; // 수수료 몇 % 적용인지 확인

    @Column(nullable = false)
    private YearMonth settlementMonth;

    @Column(nullable = false)
    private Long totalSales;

    @Column(nullable = false)
    private Long totalRefunds;

    @Column(nullable = false)
    private Long netSales;

    @Column(nullable = false)
    private Long feeAmount; // 수수료

    @Column(nullable = false)
    private Long expectedPayout; // 정산 예정 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status; // PENDING -> CONFIRMED -> PAID

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

    public void updateStatus(SettlementStatus status) {
        this.status = status;
    }
}
