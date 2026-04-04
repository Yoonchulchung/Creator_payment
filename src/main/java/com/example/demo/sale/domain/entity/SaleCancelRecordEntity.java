package com.example.demo.sale.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sale_cancel_record")
public class SaleCancelRecordEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 강의에서 부분 취소를 할 수 있는 아이템이 있으면, ManyToOne으로 변경 필요.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_record_id", nullable = false)
    private SaleRecordEntity saleRecord;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDateTime canceledAt;

    @Builder
    public SaleCancelRecordEntity(SaleRecordEntity saleRecord, Long amount, LocalDateTime canceledAt) {
        this.saleRecord = saleRecord;
        this.amount = amount;
        this.canceledAt = canceledAt;
    }
}
