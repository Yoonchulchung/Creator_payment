package com.example.demo.student.domain.repository;

import com.example.demo.student.domain.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    Optional<StudentEntity> findByName(String name);

    List<StudentEntity> findAllByDeletedAtIsNull();
}
