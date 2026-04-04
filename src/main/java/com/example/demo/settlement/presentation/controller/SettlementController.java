package com.example.demo.settlement.presentation.controller;

import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.global.presentation.code.GeneralSuccessCode;
import com.example.demo.settlement.application.service.SettlementService;
import com.example.demo.settlement.presentation.dto.SettlementAggregateResponse;
import com.example.demo.settlement.presentation.dto.SettlementSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    // 크리에이터별 월별 정산 조회
    // GET /api/settlements/monthly?creatorId=creator-xxxx&month=2025-03
    @GetMapping("/monthly")
    public ApiResponse<SettlementSummaryResponse> getMonthlySettlement(
            @RequestParam String creatorId,
            @RequestParam YearMonth month
    ) {
        SettlementSummaryResponse response = settlementService.getMonthlySettlement(creatorId, month);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }

    // 운영자용 기간별 전체 정산 집계
    // GET /api/settlements/aggregate?from=2025-01-01&to=2025-03-31
    @GetMapping("/aggregate")
    public ApiResponse<SettlementAggregateResponse> getSettlementAggregate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        SettlementAggregateResponse response = settlementService.getSettlementAggregate(from, to);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }
}
