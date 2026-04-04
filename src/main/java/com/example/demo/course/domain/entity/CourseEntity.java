package com.example.demo.course.domain.entity;

import com.example.demo.creator.domain.entity.CreatorEntity;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;

    @Builder
    public CourseEntity(String title, Long amount, CreatorEntity creator) {
        this.title = title;
        this.amount = amount;
        this.creator = creator;
    }
}
