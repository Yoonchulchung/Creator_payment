package com.example.demo.sale.presentation.dto.response;

import com.example.demo.sale.domain.entity.SaleRecordEntity;

import java.time.LocalDateTime;

public record SaleRecordResponse(
        String saleId,
        String courseId,
        String studentId,
        Long amount,
        LocalDateTime paidAt,
        String comment
) {
    public static SaleRecordResponse from(SaleRecordEntity entity) {
        return new SaleRecordResponse(
                entity.getSaleId(),
                entity.getCourse().getCourseId(),
                entity.getStudent().getStudentId(),
                entity.getAmount(),
                entity.getPaidAt(),
                entity.getComment()
        );
    }
}
