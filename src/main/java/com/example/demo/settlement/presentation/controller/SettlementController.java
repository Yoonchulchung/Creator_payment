package com.example.demo.settlement.presentation.controller;

import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.global.presentation.code.GeneralSuccessCode;
import com.example.demo.settlement.application.service.SettlementService;
import com.example.demo.settlement.presentation.dto.response.SettlementResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    // 크리에이터별 월별 정산 조회
    // GET /api/settlements/monthly?creatorId=xxxx&month=2025-03
    @GetMapping("/monthly")
    public ApiResponse<SettlementResponseDto.Summary> getMonthlySettlement(
            @RequestParam Long creatorId,
            @RequestParam YearMonth month
    ) {
        SettlementResponseDto.Summary response = settlementService.getMonthlySettlement(creatorId, month);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }

    // 운영자용 기간별 전체 정산 집계
    // GET /api/settlements/aggregate?from=2025-01-01&to=2025-03-31
    @GetMapping("/aggregate")
    public ApiResponse<List<SettlementResponseDto.Aggregate>> getSettlementAggregate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<SettlementResponseDto.Aggregate> response = settlementService.getSettlementAggregate(from, to);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }
}
