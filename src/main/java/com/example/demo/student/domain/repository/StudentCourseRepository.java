package com.example.demo.student.domain.repository;

import com.example.demo.course.domain.entity.CourseEntity;
import com.example.demo.student.domain.entity.StudentCourseEntity;
import com.example.demo.student.domain.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<StudentCourseEntity, Long> {

    List<StudentCourseEntity> findAllByStudent(StudentEntity student);

    List<StudentCourseEntity> findAllByCourse(CourseEntity course);

    Optional<StudentCourseEntity> findByStudentAndCourse(StudentEntity student, CourseEntity course);

    boolean existsByStudentAndCourse(StudentEntity student, CourseEntity course);
}
