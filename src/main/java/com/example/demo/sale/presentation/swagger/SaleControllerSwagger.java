package com.example.demo.sale.presentation.swagger;

import com.example.demo.global.infrastructure.auth.security.CustomUserDetails;
import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.sale.presentation.dto.request.SaleRequestDto;
import com.example.demo.sale.presentation.dto.response.SaleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Tag(name = "Sale", description = "판매 API")
public interface SaleControllerSwagger {

    @Operation(summary = "강의 구매 (STUDENT 전용)", description = "학생이 강의를 구매합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "구매 성공",
                    content = @Content(schema = @Schema(implementation = SaleResponseDto.Record.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음 (STUDENT만 접근 가능)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 구매한 강의")
    })
    ApiResponse<SaleResponseDto.Record> registerSale(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SaleRequestDto.Record request
    );

    @Operation(summary = "구매 취소 (STUDENT 전용)", description = "학생이 강의 구매를 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "취소 성공",
                    content = @Content(schema = @Schema(implementation = SaleResponseDto.Cancel.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음 (STUDENT만 접근 가능)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 취소된 판매")
    })
    ApiResponse<SaleResponseDto.Cancel> cancelSale(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SaleRequestDto.Cancel request
    );

    @Operation(summary = "크리에이터 판매 내역 조회 (CREATOR 전용)", description = "크리에이터의 판매 및 취소 내역을 조회합니다. 기간 필터 적용 가능.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = SaleResponseDto.SaleList.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음 (CREATOR만 접근 가능)")
    })
    ApiResponse<SaleResponseDto.SaleList> getSalesByCreator(
            @Parameter(description = "크리에이터 ID", required = true) @RequestParam Long creatorId,
            @Parameter(description = "시작 일시 (예: 2025-01-01T00:00:00)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "종료 일시 (예: 2025-03-31T23:59:59)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    );
}
