package com.example.demo.settlement.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "fee")
public class FeeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double feeRate;

    @Column(nullable = false)
    private LocalDateTime applyAt;

    @Builder
    public FeeEntity(Double feeRate, LocalDateTime applyAt) {
        this.feeRate = feeRate;
        this.applyAt = applyAt;
    }
}
