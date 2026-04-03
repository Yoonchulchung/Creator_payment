package com.example.demo.creator.domain.repository;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.creator.domain.entity.CreatorCourseEntity;
import com.example.demo.creator.domain.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreatorCourseRepository extends JpaRepository<CreatorCourseEntity, Long> {

    List<CreatorCourseEntity> findAllByCreator(CreatorEntity creator);

    List<CreatorCourseEntity> findAllByCourse(CourseEntity course);

    Optional<CreatorCourseEntity> findByCreatorAndCourse(CreatorEntity creator, CourseEntity course);

    boolean existsByCreatorAndCourse(CreatorEntity creator, CourseEntity course);
}
