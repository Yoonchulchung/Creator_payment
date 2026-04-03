package com.example.demo.sale.domain.repository;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.sale.domain.entity.SaleRecordEntity;
import com.example.demo.student.domain.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRecordRepository extends JpaRepository<SaleRecordEntity, Long> {

    Optional<SaleRecordEntity> findBySaleId(String saleId);

    List<SaleRecordEntity> findAllByStudent(StudentEntity student);

    List<SaleRecordEntity> findAllByCourse(CourseEntity course);

    List<SaleRecordEntity> findAllByCourseIn(List<CourseEntity> courses);

    List<SaleRecordEntity> findAllByCourseInAndPaidAtBetween(List<CourseEntity> courses, LocalDateTime from, LocalDateTime to);

    boolean existsByStudentAndCourse(StudentEntity student, CourseEntity course);
}
