package com.example.demo.sale.application.service;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.course.domain.repository.CourseRepository;
import com.example.demo.creator.domain.entity.CreatorEntity;
import com.example.demo.creator.domain.repository.CreatorRepository;
import com.example.demo.sale.domain.entity.SaleCancelRecordEntity;
import com.example.demo.sale.domain.entity.SaleRecordEntity;
import com.example.demo.sale.domain.repository.SaleCancelRecordRepository;
import com.example.demo.sale.domain.repository.SaleRecordRepository;
import com.example.demo.sale.presentation.dto.response.SaleResponseDto;
import com.example.demo.sale.presentation.dto.request.SaleRequestDto;
import com.example.demo.student.domain.entity.StudentEntity;
import com.example.demo.student.domain.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRecordRepository saleRecordRepository;
    private final SaleCancelRecordRepository saleCancelRecordRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CreatorRepository creatorRepository;

    @Transactional
    public SaleResponseDto.Record registerSale(Long userId, SaleRequestDto.Record request) {
        
        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("학생 권한이 없습니다."));

        CourseEntity course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));

        if (saleRecordRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("이미 구매한 강의입니다.");
        }

        SaleRecordEntity saleRecord = SaleRecordEntity.builder()
                .course(course)
                .student(student)
                .amount(course.getAmount())
                .build();

        SaleRecordEntity saved = saleRecordRepository.save(saleRecord);

        return SaleResponseDto.Record.builder()
                .saleId(saved.getId())
                .courseId(saved.getCourse().getId())
                .studentId(saved.getStudent().getId())
                .amount(saved.getAmount())
                .paidAt(saved.getPaidAt())
                .build();
    }

    @Transactional
    public SaleResponseDto.Cancel cancelSale(Long userId, SaleRequestDto.Cancel request) {

        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("학생 권한이 없습니다."));
                
        SaleRecordEntity saleRecord = saleRecordRepository.findById(request.saleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 내역입니다."));

        // 강의에서 선택적으로 환불 요청이 필요하면 아래 조건문 삭제 필요.
        if (saleCancelRecordRepository.existsBySaleRecord(saleRecord)) {
            throw new IllegalStateException("이미 취소된 판매 내역입니다.");
        }

        // amount는 BE에서 관리하는게 맞는데, 환불 정책을 모르기 때문에 얼마큼 빼야할지 몰라 FE에서 요청하도록 구성.
        SaleCancelRecordEntity cancelRecord = SaleCancelRecordEntity.builder()
                .saleRecord(saleRecord)
                .amount(request.amount())
                .build();

        SaleCancelRecordEntity saved = saleCancelRecordRepository.save(cancelRecord);

        return SaleResponseDto.Cancel.builder()
                .saleId(saved.getSaleRecord().getId())
                .amount(saved.getAmount())
                .canceledAt(saved.getCanceledAt())
                .build();
    }

    // *** //
    // 조회 //
    // *** //
    @Transactional(readOnly = true)
    public SaleResponseDto.SaleList getSalesByCreator(Long creatorId, LocalDateTime from, LocalDateTime to) {
        CreatorEntity creator = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다."));

        List<CourseEntity> courses = courseRepository.findAllByCreator(creator);

        List<SaleRecordEntity> saleRecords = (from != null && to != null)
                ? saleRecordRepository.findAllByCourseInAndPaidAtBetween(courses, from, to)
                : saleRecordRepository.findAllByCourseIn(courses);

        List<SaleResponseDto.Record> records = saleRecords.stream()
                .map(s -> SaleResponseDto.Record.builder()
                        .saleId(s.getId())
                        .courseId(s.getCourse().getId())
                        .studentId(s.getStudent().getId())
                        .amount(s.getAmount())
                        .paidAt(s.getPaidAt())
                        .build())
                .toList();

        List<SaleResponseDto.Cancel> cancels = saleCancelRecordRepository.findAllBySaleRecordIn(saleRecords)
                .stream()
                .map(c -> SaleResponseDto.Cancel.builder()
                        .saleId(c.getSaleRecord().getId())
                        .amount(c.getAmount())
                        .canceledAt(c.getCanceledAt())
                        .build())
                .toList();

        return SaleResponseDto.SaleList.builder()
                .record(records)
                .cancel(cancels)
                .build();
    }


}
