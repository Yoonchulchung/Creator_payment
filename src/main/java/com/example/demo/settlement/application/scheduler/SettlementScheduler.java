package com.example.demo.settlement.application.scheduler;

import com.example.demo.settlement.application.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

    private final SettlementService settlementService;

    // 매월 1일 00:05:00 (KST) 에 전월 정산 실행
    @Scheduled(cron = "0 5 0 1 * *", zone = "Asia/Seoul")
    public void runMonthlySettlement() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        log.info("정산 스케줄러 시작: targetMonth={}", lastMonth);
        settlementService.generateSettlement(lastMonth);
        log.info("정산 스케줄러 완료: targetMonth={}", lastMonth);
    }
}
