package com.example.demo.settlement.presentation.dto.response;

import java.time.YearMonth;

public record SettlementSummaryResponse(
        Long creatorId,
        YearMonth settlementMonth,
        long totalSales,
        long totalRefunds,
        long netSales,
        long feeAmount,
        long expectedPayout,
        int saleCount,
        int cancelCount
) {}
