package com.example.demo.sale.presentation.dto;

import java.time.LocalDateTime;

public record SaleCancelRequest(
        String saleId,
        Long amount,
        LocalDateTime canceledAt
) {}
