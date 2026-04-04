package com.example.demo.sale.presentation.dto.request;

import java.time.LocalDateTime;

public record SaleRecordRequest(
        Long courseId,
        Long amount
) {}
