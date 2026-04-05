package com.example.demo.settlement.presentation.controller;

import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.global.presentation.code.GeneralSuccessCode;
import com.example.demo.settlement.application.service.SettlementService;
import com.example.demo.settlement.presentation.dto.response.SettlementResponseDto;
import com.example.demo.settlement.presentation.swagger.SettlementControllerSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class SettlementController implements SettlementControllerSwagger {

    private final SettlementService settlementService;

    // 크리에이터별 월별 정산 조회
    @GetMapping("/inquiry/monthly")
    public ApiResponse<SettlementResponseDto.MonthlyInquiry> getMonthlySettlement(
            @RequestParam Long creatorId,
            @RequestParam YearMonth month
    ) {
        SettlementResponseDto.MonthlyInquiry response = settlementService.getMonthlySettlement(creatorId, month);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }

    // 운영자용 기간별 전체 정산 집계
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/aggregate")
    public ApiResponse<List<SettlementResponseDto.Aggregate>> getSettlementAggregate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<SettlementResponseDto.Aggregate> response = settlementService.getSettlementAggregate(from, to);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }
}
