package com.example.demo.sale.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SaleResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cancel {
        private Long saleId;
        private Long amount;
        private LocalDateTime canceledAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record {
        private Long saleId;
        private Long courseId;
        private Long studentId;
        private Long amount;
        private LocalDateTime paidAt;
    }
}
