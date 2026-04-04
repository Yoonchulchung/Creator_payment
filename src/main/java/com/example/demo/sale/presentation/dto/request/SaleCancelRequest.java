package com.example.demo.sale.presentation.dto.request;

import java.time.LocalDateTime;

public record SaleCancelRequest(
        String saleId,
        Long amount,
        LocalDateTime canceledAt
) {}
