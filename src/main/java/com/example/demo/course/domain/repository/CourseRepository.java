package com.example.demo.course.domain.repository;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.creator.domain.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    Optional<CourseEntity> findByTitle(String title);

    List<CourseEntity> findAllByCreator(CreatorEntity creator);

    List<CourseEntity> findAllByDeletedAtIsNull();
}
