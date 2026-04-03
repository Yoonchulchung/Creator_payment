package com.example.demo.settlement.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettlementStatus {

    PENDING("정산 대기 중"),
    CONFIRMED("정산 승인 완료"),
    PAID("정산 완료");

    private final String description;
}
