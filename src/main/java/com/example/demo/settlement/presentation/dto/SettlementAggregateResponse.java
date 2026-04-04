package com.example.demo.settlement.presentation.dto;

import java.time.LocalDate;
import java.util.List;

public record SettlementAggregateResponse(
        LocalDate from,
        LocalDate to,
        List<CreatorSettlementSummary> settlements,
        long totalExpectedPayout
) {
    public record CreatorSettlementSummary(
            String creatorId,
            String creatorName,
            long totalSales,
            long totalRefunds,
            long netSales,
            long feeAmount,
            long expectedPayout,
            int saleCount,
            int cancelCount
    ) {}
}
