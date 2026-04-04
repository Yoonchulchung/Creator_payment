package com.example.demo.sale.presentation.dto.response;

import com.example.demo.sale.domain.entity.SaleCancelRecordEntity;

import java.time.LocalDateTime;

public record SaleCancelResponse(
        String saleId,
        Long amount,
        LocalDateTime canceledAt
) {
    public static SaleCancelResponse from(SaleCancelRecordEntity entity) {
        return new SaleCancelResponse(
                entity.getSaleRecord().getSaleId(),
                entity.getAmount(),
                entity.getCanceledAt()
        );
    }
}
