package com.example.demo.settlement.domain.repository;

import com.example.demo.creator.domain.entity.CreatorEntity;
import com.example.demo.settlement.domain.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface SettlementRepository extends JpaRepository<SettlementEntity, Long> {

    boolean existsByCreatorAndSettlementMonth(CreatorEntity creator, YearMonth settlementMonth);

    Optional<SettlementEntity> findByCreatorAndSettlementMonth(CreatorEntity creator, YearMonth settlementMonth);

    List<SettlementEntity> findAllBySettlementMonth(YearMonth settlementMonth);

    List<SettlementEntity> findAllBySettlementMonthBetween(YearMonth from, YearMonth to);
}
