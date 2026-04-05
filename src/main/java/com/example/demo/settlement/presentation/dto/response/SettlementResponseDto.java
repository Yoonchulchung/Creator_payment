package com.example.demo.settlement.presentation.dto.response;

import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SettlementResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Aggregate {
        private long totalSales;
        private long totalRefunds;
        private long netSales;
        private long feeAmount;
        private long expectedPayout;
        private int saleCount;
        private int cancelCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyInquiry {
        private Long creatorId;
        private YearMonth settlementMonth;
        private long totalSales;
        private long totalRefunds;
        private long netSales;
        private long feeAmount;
        private long expectedPayout;
        private int saleCount;
        private int cancelCount;
    }
}
