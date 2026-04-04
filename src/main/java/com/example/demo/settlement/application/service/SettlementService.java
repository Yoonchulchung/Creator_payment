package com.example.demo.settlement.application.service;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.creator.domain.entity.CreatorCourseEntity;
import com.example.demo.creator.domain.entity.CreatorEntity;
import com.example.demo.creator.domain.repository.CreatorCourseRepository;
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
import com.example.demo.settlement.presentation.dto.SettlementAggregateResponse;
import com.example.demo.settlement.presentation.dto.SettlementSummaryResponse;
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
    private final CreatorCourseRepository creatorCourseRepository;
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
                log.info("이미 정산 완료: creator={}, month={}", creator.getCreatorId(), targetMonth);
                continue;
            }

            List<CourseEntity> courses = creatorCourseRepository.findAllByCreator(creator)
                    .stream()
                    .map(CreatorCourseEntity::getCourse)
                    .toList();

            if (courses.isEmpty()) continue;

            List<SaleRecordEntity> sales = saleRecordRepository
                    .findAllByCourseInAndPaidAtBetween(courses, from, to);

            long totalSales = sales.stream().mapToLong(SaleRecordEntity::getAmount).sum();

            List<SaleCancelRecordEntity> cancels = saleCancelRecordRepository.findAllBySaleRecordIn(sales);
            long totalRefunds = cancels.stream().mapToLong(SaleCancelRecordEntity::getAmount).sum();

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
                    creator.getCreatorId(), targetMonth, expectedPayout);
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
    public SettlementSummaryResponse getMonthlySettlement(String creatorId, YearMonth month) {
        CreatorEntity creator = creatorRepository.findByCreatorId(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다."));

        LocalDateTime from = month.atDay(1).atStartOfDay();
        LocalDateTime to = month.atEndOfMonth().atTime(23, 59, 59);

        List<CourseEntity> courses = creatorCourseRepository.findAllByCreator(creator)
                .stream().map(CreatorCourseEntity::getCourse).toList();

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

        return new SettlementSummaryResponse(
                creatorId, month,
                totalSales, totalRefunds, netSales,
                feeAmount, expectedPayout,
                sales.size(), cancels.size()
        );
    }

    @Transactional(readOnly = true)
    public SettlementAggregateResponse getSettlementAggregate(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.atTime(23, 59, 59);

        List<CreatorEntity> creators = creatorRepository.findAll();

        List<SettlementAggregateResponse.CreatorSettlementSummary> summaries = creators.stream()
                .map(creator -> {
                    List<CourseEntity> courses = creatorCourseRepository.findAllByCreator(creator)
                            .stream().map(CreatorCourseEntity::getCourse).toList();

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

                    return new SettlementAggregateResponse.CreatorSettlementSummary(
                            creator.getCreatorId(), creator.getName(),
                            totalSales, totalRefunds, netSales,
                            feeAmount, expectedPayout,
                            sales.size(), cancels.size()
                    );
                })
                .filter(s -> s.saleCount() > 0)
                .toList();

        long totalExpectedPayout = summaries.stream()
                .mapToLong(SettlementAggregateResponse.CreatorSettlementSummary::expectedPayout)
                .sum();

        return new SettlementAggregateResponse(from, to, summaries, totalExpectedPayout);
    }
}
