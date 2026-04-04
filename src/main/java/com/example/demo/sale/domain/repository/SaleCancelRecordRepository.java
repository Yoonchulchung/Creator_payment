package com.example.demo.sale.domain.repository;

import com.example.demo.sale.domain.entity.SaleCancelRecordEntity;
import com.example.demo.sale.domain.entity.SaleRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleCancelRecordRepository extends JpaRepository<SaleCancelRecordEntity, Long> {

    List<SaleCancelRecordEntity> findAllBySaleRecord(SaleRecordEntity saleRecord);

    List<SaleCancelRecordEntity> findAllBySaleRecordIn(List<SaleRecordEntity> saleRecords);

    boolean existsBySaleRecord(SaleRecordEntity saleRecord);
}
