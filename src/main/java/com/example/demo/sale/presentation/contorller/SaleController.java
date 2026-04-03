package com.example.demo.sale.presentation.contorller;

import com.example.demo.global.presentation.ApiResponse;
import com.example.demo.global.presentation.code.GeneralSuccessCode;
import com.example.demo.sale.application.service.SaleService;
import com.example.demo.sale.presentation.dto.request.SaleCancelRequest;
import com.example.demo.sale.presentation.dto.response.SaleCancelResponse;
import com.example.demo.sale.presentation.dto.request.SaleRecordRequest;
import com.example.demo.sale.presentation.dto.response.SaleRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ApiResponse<SaleRecordResponse> registerSale(@RequestBody SaleRecordRequest request) {
        SaleRecordResponse response = saleService.registerSale(request);
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATE, response);
    }

    @PostMapping("/cancel")
    public ApiResponse<SaleCancelResponse> cancelSale(@RequestBody SaleCancelRequest request) {
        SaleCancelResponse response = saleService.cancelSale(request);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }

    @GetMapping
    public ApiResponse<List<SaleRecordResponse>> getSalesByCreator(
            @RequestParam String creatorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        List<SaleRecordResponse> response = saleService.getSalesByCreator(creatorId, from, to);
        return ApiResponse.onSuccess(GeneralSuccessCode.GOOD_REQUEST, response);
    }
}
