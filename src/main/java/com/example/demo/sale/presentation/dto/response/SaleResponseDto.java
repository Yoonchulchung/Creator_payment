package com.example.demo.sale.presentation.dto.response;

import com.example.demo.sale.domain.entity.SaleCancelRecordEntity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDto{

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelDto {
        private Long saleId;
        private Long amount;
        private LocalDateTime canceledAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordDto {
        private Long saleId;
        private Long courseId;
        private Long studentId;
        private Long amount;
        private LocalDateTime paidAt;
    }
}
