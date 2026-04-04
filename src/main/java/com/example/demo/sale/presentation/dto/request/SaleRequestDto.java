package com.example.demo.sale.presentation.dto.request;

import java.time.LocalDateTime;

public class SaleRequestDto {

    public record Record(Long courseId, Long amount) {}

    public record Cancel(Long saleId, Long amount, LocalDateTime canceledAt) {}
}
