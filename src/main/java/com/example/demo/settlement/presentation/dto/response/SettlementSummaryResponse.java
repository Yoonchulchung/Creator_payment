package com.example.demo.settlement.presentation.dto;

import java.time.YearMonth;

public record SettlementSummaryResponse(
        String creatorId,
        YearMonth settlementMonth,
        long totalSales,
        long totalRefunds,
        long netSales,
        long feeAmount,
        long expectedPayout,
        int saleCount,
        int cancelCount
) {}
