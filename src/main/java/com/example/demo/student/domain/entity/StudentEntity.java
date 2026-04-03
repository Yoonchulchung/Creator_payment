package com.example.demo.student.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class StudentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String studentId;

    @Column(nullable = false)
    private String name;

    @Builder
    public StudentEntity(String name) {
        this.name = name;
    }

    @PrePersist
    private void generateStudentId() {
        this.studentId = "student-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
