package com.example.demo.sale.domain.entity;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.global.entity.BaseEntity;
import com.example.demo.student.domain.entity.StudentEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sale_record")
public class SaleRecordEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @Builder
    public SaleRecordEntity(CourseEntity course, StudentEntity student, Long amount) {
        this.course = course;
        this.student = student;
        this.amount = amount;
        this.paidAt = LocalDateTime.now();
    }
}
