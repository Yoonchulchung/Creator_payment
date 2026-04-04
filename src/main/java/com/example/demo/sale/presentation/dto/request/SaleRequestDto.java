package com.example.demo.sale.presentation.dto.request;

import java.time.LocalDateTime;

public class SaleRequestDto {

    public record Record(Long courseId) {}

    public record Cancel(Long saleId) {}
}
