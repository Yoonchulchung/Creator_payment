package com.example.demo.settlement.application.service;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.course.domain.repository.CourseRepository;
import com.example.demo.creator.domain.entity.CreatorEntity;
import com.example.demo.creator.domain.repository.CreatorRepository;
import com.example.demo.sale.domain.entity.SaleCancelRecordEntity;
import com.example.demo.sale.domain.entity.SaleRecordEntity;
import com.example.demo.sale.domain.repository.SaleCancelRecordRepository;
import com.example.demo.sale.domain.repository.SaleRecordRepository;
import com.example.demo.settlement.domain.entity.FeeEntity;
import com.example.demo.settlement.domain.entity.SettlementEntity;
import com.example.demo.settlement.domain.enums.SettlementStatus;
import com.example.demo.settlement.domain.repository.FeeRepository;
import com.example.demo.settlement.domain.repository.SettlementRepository;
import com.example.demo.settlement.presentation.dto.response.SettlementResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final CreatorRepository creatorRepository;
    private final CourseRepository courseRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final SaleCancelRecordRepository saleCancelRecordRepository;
    private final FeeRepository feeRepository;
    private final SettlementRepository settlementRepository;

    // *********** //
    // 스케줄러로 관리 //
    // *********** //
    @Transactional
    public void generateSettlement(YearMonth targetMonth) {
        LocalDateTime from = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime to = targetMonth.atEndOfMonth().atTime(23, 59, 59);

        FeeEntity fee = feeRepository.findTopByApplyAtLessThanEqualOrderByApplyAtDesc(to)
                .orElseThrow(() -> new IllegalStateException("적용 가능한 수수료 정책이 없습니다."));

        List<CreatorEntity> creators = creatorRepository.findAll();

        for (CreatorEntity creator : creators) {
            if (settlementRepository.existsByCreatorAndSettlementMonth(creator, targetMonth)) {
                log.info("이미 정산 완료: creator={}, month={}", creator.getId(), targetMonth);
                continue;
            }

            // creator의 담당 course 확인.
            List<CourseEntity> courses = courseRepository.findAllByCreator(creator);

            if (courses.isEmpty()) continue;

            // 결제 기록 확인.
            List<SaleRecordEntity> sales = saleRecordRepository
                    .findAllByCourseInAndPaidAtBetween(courses, from, to);

            long totalSales = sales.stream().mapToLong(SaleRecordEntity::getAmount).sum();

            List<SaleCancelRecordEntity> cancels = saleCancelRecordRepository.findAllBySaleRecordIn(sales);
            long totalRefunds = cancels.stream().mapToLong(SaleCancelRecordEntity::getAmount).sum();

            // 최종 정산 로직
            long netSales = totalSales - totalRefunds;
            long feeAmount = (long) (netSales * fee.getFeeRate());
            long expectedPayout = netSales - feeAmount;

            SettlementEntity settlement = SettlementEntity.builder()
                    .creator(creator)
                    .fee(fee)
                    .settlementMonth(targetMonth)
                    .totalSales(totalSales)
                    .totalRefunds(totalRefunds)
                    .netSales(netSales)
                    .feeAmount(feeAmount)
                    .expectedPayout(expectedPayout)
                    .build();

            settlementRepository.save(settlement);
            log.info("정산 생성 완료: creator={}, month={}, expectedPayout={}",
                    creator.getId(), targetMonth, expectedPayout);
        }
    }

    // ******* //
    // 상태 변경 //
    // ******* //
    @Transactional
    public void confirmSettlement(Long settlementId) {
        SettlementEntity settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정산입니다."));

        if (settlement.getStatus() != SettlementStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태의 정산만 승인할 수 있습니다.");
        }

        settlement.updateStatus(SettlementStatus.CONFIRMED);
        log.info("정산 승인 완료: settlementId={}", settlementId);
    }

    @Transactional
    public void paidSettlement(Long settlementId) {
        SettlementEntity settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정산입니다."));

        if (settlement.getStatus() != SettlementStatus.CONFIRMED) {
            throw new IllegalStateException("CONFIRMED 상태의 정산만 지급 완료 처리할 수 있습니다.");
        }

        settlement.updateStatus(SettlementStatus.PAID);
        log.info("정산 지급 완료: settlementId={}", settlementId);
    }

    // **** //
    // 조회 //
    // **** //
    @Transactional(readOnly = true)
    public SettlementResponseDto.MonthlyInquiry getMonthlySettlement(Long creatorId, YearMonth month) {
        CreatorEntity creator = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다."));

        LocalDateTime from = month.atDay(1).atStartOfDay();
        LocalDateTime to = month.atEndOfMonth().atTime(23, 59, 59);

        List<CourseEntity> courses = courseRepository.findAllByCreator(creator);

        List<SaleRecordEntity> sales = courses.isEmpty()
                ? List.of()
                : saleRecordRepository.findAllByCourseInAndPaidAtBetween(courses, from, to);

        List<SaleCancelRecordEntity> cancels = sales.isEmpty()
                ? List.of()
                : saleCancelRecordRepository.findAllBySaleRecordIn(sales);

        long totalSales = sales.stream().mapToLong(SaleRecordEntity::getAmount).sum();
        long totalRefunds = cancels.stream().mapToLong(SaleCancelRecordEntity::getAmount).sum();
        long netSales = totalSales - totalRefunds;
        long feeAmount = (long) (netSales * 0.20);
        long expectedPayout = netSales - feeAmount;

        return SettlementResponseDto.MonthlyInquiry.builder()
                .creatorId(creatorId)
                .settlementMonth(month)
                .totalSales(totalSales)
                .totalRefunds(totalRefunds)
                .netSales(netSales)
                .feeAmount(feeAmount)
                .expectedPayout(expectedPayout)
                .saleCount(sales.size())
                .cancelCount(cancels.size())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SettlementResponseDto.Aggregate> getSettlementAggregate(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.atTime(23, 59, 59);

        return creatorRepository.findAll().stream()
                .map(creator -> {
                    List<CourseEntity> courses = courseRepository.findAllByCreator(creator);

                    List<SaleRecordEntity> sales = courses.isEmpty()
                            ? List.of()
                            : saleRecordRepository.findAllByCourseInAndPaidAtBetween(courses, fromDt, toDt);

                    List<SaleCancelRecordEntity> cancels = sales.isEmpty()
                            ? List.of()
                            : saleCancelRecordRepository.findAllBySaleRecordIn(sales);

                    long totalSales = sales.stream().mapToLong(SaleRecordEntity::getAmount).sum();
                    long totalRefunds = cancels.stream().mapToLong(SaleCancelRecordEntity::getAmount).sum();
                    long netSales = totalSales - totalRefunds;
                    long feeAmount = (long) (netSales * 0.20);
                    long expectedPayout = netSales - feeAmount;

                    return SettlementResponseDto.Aggregate.builder()
                            .creatorId(creator.getId())
                            .creatorName(creator.getName())
                            .totalSales(totalSales)
                            .totalRefunds(totalRefunds)
                            .netSales(netSales)
                            .feeAmount(feeAmount)
                            .expectedPayout(expectedPayout)
                            .saleCount(sales.size())
                            .cancelCount(cancels.size())
                            .build();
                })
                .filter(s -> s.getSaleCount() > 0)
                .toList();
    }
}
