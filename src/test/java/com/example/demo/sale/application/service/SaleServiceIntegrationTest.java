package com.example.demo.sale.application.service;

import com.example.demo.sale.presentation.dto.response.SaleResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SaleServiceIntegrationTest {

    @Autowired
    private SaleService saleService;

    // creator-1(id=1): course-1(50000), course-2(80000)
    // creator-2(id=2): course-3(60000)
    // creator-3(id=3): course-4(120000)

    @Test
    @DisplayName("케이스1,2 — creator-1의 3월 정상 판매 2건 (course-1, student-1,2)")
    void creator1_march_normalSales() {
        LocalDateTime from = LocalDateTime.of(2025, 3, 1, 0, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 3, 31, 23, 59, 59);

        SaleResponseDto.SaleList result = saleService.getSalesByCreator(1L, from, to);

        // course-1: sale-1(student-1), sale-2(student-2)
        // course-2: sale-3(student-3), sale-4(student-4)
        assertThat(result.getRecords()).hasSize(4);
        assertThat(result.getCancels()).isEmpty();
        assertThat(result.getRecords())
                .extracting(SaleResponseDto.Record::getAmount)
                .containsExactlyInAnyOrder(50000L, 50000L, 80000L, 80000L);
    }

    @Test
    @DisplayName("케이스3 — creator-1의 course-2 sale-3 전액 환불 대상 확인")
    void creator1_course2_fullRefundTarget() {
        // 3월 전체 조회 후 sale-3(id=3)이 포함되는지 확인
        SaleResponseDto.SaleList result = saleService.getSalesByCreator(1L, null, null);

        assertThat(result.getRecords())
                .anyMatch(r -> r.getAmount() == 80000L && r.getStudentId() == 3L);
    }

    @Test
    @DisplayName("케이스5 — 월 경계: sale-5는 1월 결제이므로 3월 조회에서 제외")
    void creator2_march_excludesJanuarySale() {
        LocalDateTime from = LocalDateTime.of(2025, 3, 1, 0, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 3, 31, 23, 59, 59);

        SaleResponseDto.SaleList result = saleService.getSalesByCreator(2L, from, to);

        // sale-5(1월 결제)는 제외, sale-6(3월 결제)만 포함
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getAmount()).isEqualTo(60000L);
    }

    @Test
    @DisplayName("케이스5 — 월 경계: sale-5는 1월 조회에서 포함")
    void creator2_january_includesJanuarySale() {
        LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        SaleResponseDto.SaleList result = saleService.getSalesByCreator(2L, from, to);

        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getAmount()).isEqualTo(60000L);
    }

    @Test
    @DisplayName("케이스6 — creator-2의 3월 정상 판매 1건 (course-3, student-6)")
    void creator2_march_normalSale() {
        LocalDateTime from = LocalDateTime.of(2025, 3, 1, 0, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 3, 31, 23, 59, 59);

        SaleResponseDto.SaleList result = saleService.getSalesByCreator(2L, from, to);

        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getStudentId()).isEqualTo(6L);
    }

    @Test
    @DisplayName("케이스7 — creator-3의 3월 조회: 판매 없음 (2월에만 판매)")
    void creator3_march_empty() {
        LocalDateTime from = LocalDateTime.of(2025, 3, 1, 0, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 3, 31, 23, 59, 59);

        SaleResponseDto.SaleList result = saleService.getSalesByCreator(3L, from, to);

        assertThat(result.getRecords()).isEmpty();
        assertThat(result.getCancels()).isEmpty();
    }

    @Test
    @DisplayName("케이스7 — creator-3의 2월 조회: sale-7 포함")
    void creator3_february_hasSale() {
        LocalDateTime from = LocalDateTime.of(2025, 2, 1, 0, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 2, 28, 23, 59, 59);

        SaleResponseDto.SaleList result = saleService.getSalesByCreator(3L, from, to);

        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getAmount()).isEqualTo(120000L);
    }

    @Test
    @DisplayName("기간 필터 없이 전체 조회")
    void creator1_noFilter_allSales() {
        SaleResponseDto.SaleList result = saleService.getSalesByCreator(1L, null, null);

        // course-1(sale-1,2), course-2(sale-3,4) 총 4건
        assertThat(result.getRecords()).hasSize(4);
    }
}
