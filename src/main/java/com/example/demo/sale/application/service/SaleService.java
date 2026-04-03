package com.example.demo.sale.application.service;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.course.domain.repository.CourseRepository;
import com.example.demo.creator.domain.entity.CreatorEntity;
import com.example.demo.creator.domain.repository.CreatorCourseRepository;
import com.example.demo.creator.domain.repository.CreatorRepository;
import com.example.demo.sale.domain.entity.SaleCancelRecordEntity;
import com.example.demo.sale.domain.entity.SaleRecordEntity;
import com.example.demo.sale.domain.repository.SaleCancelRecordRepository;
import com.example.demo.sale.domain.repository.SaleRecordRepository;
import com.example.demo.sale.presentation.dto.SaleCancelRequest;
import com.example.demo.sale.presentation.dto.SaleCancelResponse;
import com.example.demo.sale.presentation.dto.SaleRecordRequest;
import com.example.demo.sale.presentation.dto.SaleRecordResponse;
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
    private final CreatorCourseRepository creatorCourseRepository;

    @Transactional
    public SaleRecordResponse registerSale(SaleRecordRequest request) {
        CourseEntity course = courseRepository.findByCourseId(request.courseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));

        StudentEntity student = studentRepository.findByStudentId(request.studentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        if (saleRecordRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("이미 구매한 강의입니다.");
        }

        SaleRecordEntity saleRecord = SaleRecordEntity.builder()
                .course(course)
                .student(student)
                .amount(request.amount())
                .paidAt(request.paidAt())
                .comment(request.comment())
                .build();

        return SaleRecordResponse.from(saleRecordRepository.save(saleRecord));
    }

    @Transactional
    public SaleCancelResponse cancelSale(SaleCancelRequest request) {
        SaleRecordEntity saleRecord = saleRecordRepository.findBySaleId(request.saleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 내역입니다."));

        if (saleCancelRecordRepository.existsBySaleRecord(saleRecord)) {
            throw new IllegalStateException("이미 취소된 판매 내역입니다.");
        }

        SaleCancelRecordEntity cancelRecord = SaleCancelRecordEntity.builder()
                .saleRecord(saleRecord)
                .amount(request.amount())
                .canceledAt(request.canceledAt())
                .build();

        return SaleCancelResponse.from(saleCancelRecordRepository.save(cancelRecord));
    }

    @Transactional(readOnly = true)
    public List<SaleRecordResponse> getSalesByCreator(String creatorId, LocalDateTime from, LocalDateTime to) {
        CreatorEntity creator = creatorRepository.findByCreatorId(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다."));

        List<CourseEntity> courses = creatorCourseRepository.findAllByCreator(creator)
                .stream()
                .map(cc -> cc.getCourse())
                .toList();

        List<SaleRecordEntity> saleRecords = (from != null && to != null)
                ? saleRecordRepository.findAllByCourseInAndPaidAtBetween(courses, from, to)
                : saleRecordRepository.findAllByCourseIn(courses);

        return saleRecords.stream()
                .map(SaleRecordResponse::from)
                .toList();
    }
}
