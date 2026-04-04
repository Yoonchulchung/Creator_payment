package com.example.demo.sale.presentation.contorller;

import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.global.presentation.code.GeneralSuccessCode;
import com.example.demo.sale.application.service.SaleService;
import com.example.demo.sale.presentation.dto.request.SaleRequestDto;
import com.example.demo.sale.presentation.dto.response.SaleResponseDto;
import com.example.demo.global.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ApiResponse<SaleResponseDto.Record> registerSale(    // 학생만 요청
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SaleRequestDto.Record request
    ) {
        SaleResponseDto.Record response = saleService.registerSale(userDetails.getUserId(), request);
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATE, response);
    }

    @PostMapping("/cancel")
    public ApiResponse<SaleResponseDto.Cancel> cancelSale(    // 학생만 요청
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SaleRequestDto.Cancel request
    ) {
        SaleResponseDto.Cancel response = saleService.cancelSale(userDetails.getUserId(), request);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }


    // *** //
    // 조회 //
    // *** //
    // @GetMapping
    // public ApiResponse<List<SaleResponseDto.Record>> getSalesByCreator(
    //         @RequestParam Long creatorId,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    // ) {
    //     List<SaleResponseDto.Record> response = saleService.getSalesByCreator(creatorId, from, to);
    //     return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    // }
}
