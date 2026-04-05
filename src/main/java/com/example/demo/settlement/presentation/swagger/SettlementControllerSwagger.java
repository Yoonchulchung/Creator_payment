package com.example.demo.settlement.presentation.swagger;

import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.settlement.presentation.dto.response.SettlementResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "Settlement", description = "정산 API")
public interface SettlementControllerSwagger {

    @Operation(summary = "크리에이터 월별 정산 조회", description = "특정 크리에이터의 월별 정산 내역을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = SettlementResponseDto.MonthlyInquiry.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "크리에이터 없음")
    })
    ApiResponse<SettlementResponseDto.MonthlyInquiry> getMonthlySettlement(
            @Parameter(description = "크리에이터 ID", required = true) @RequestParam Long creatorId,
            @Parameter(description = "조회 월 (예: 2025-03)", required = true) @RequestParam YearMonth month
    );

    @Operation(summary = "기간별 전체 정산 집계 (ADMIN 전용)", description = "운영자가 기간별 전체 크리에이터 정산을 집계합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "집계 성공",
                    content = @Content(schema = @Schema(implementation = SettlementResponseDto.Aggregate.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음 (ADMIN만 접근 가능)")
    })
    ApiResponse<List<SettlementResponseDto.Aggregate>> getSettlementAggregate(
            @Parameter(description = "시작일 (예: 2025-01-01)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "종료일 (예: 2025-03-31)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    );
}
