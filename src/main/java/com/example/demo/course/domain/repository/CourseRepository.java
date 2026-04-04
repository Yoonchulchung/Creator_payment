package com.example.demo.course.domain.repository;

import com.example.demo.course.domain.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    Optional<CourseEntity> findByTitle(String title);

    List<CourseEntity> findAllByDeletedAtIsNull();
}
