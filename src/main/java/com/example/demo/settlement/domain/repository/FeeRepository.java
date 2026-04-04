package com.example.demo.settlement.domain.repository;

import com.example.demo.settlement.domain.entity.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<FeeEntity, Long> {

    Optional<FeeEntity> findTopByApplyAtLessThanEqualOrderByApplyAtDesc(LocalDateTime dateTime);
}
