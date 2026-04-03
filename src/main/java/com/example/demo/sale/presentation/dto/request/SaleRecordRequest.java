package com.example.demo.sale.presentation.dto;

import java.time.LocalDateTime;

public record SaleRecordRequest(
        String courseId,
        String studentId,
        Long amount,
        LocalDateTime paidAt,
        String comment
) {}
