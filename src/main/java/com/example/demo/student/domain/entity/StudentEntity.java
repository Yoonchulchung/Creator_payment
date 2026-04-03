package com.example.demo.student.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import com.example.demo.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, unique = true, updatable = false)
    private String studentId;

    @Column(nullable = false)
    private String name;

    @Builder
    public StudentEntity(UserEntity user, String name) {
        this.user = user;
        this.name = name;
    }

    @PrePersist
    private void generateStudentId() {
        this.studentId = "student-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
