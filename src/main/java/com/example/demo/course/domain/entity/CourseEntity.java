package com.example.demo.course.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "course")
public class CourseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String courseId;

    @Column(nullable = false)
    private String title;

    @Builder
    public CourseEntity(String title) {
        this.title = title;
    }

    @PrePersist
    private void generateCourseId() {
        this.courseId = "course-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
