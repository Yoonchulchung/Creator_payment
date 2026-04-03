package com.example.demo.creator.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "creator")
public class CreatorEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String creatorId;

    @Column(nullable = false)
    private String name;

    @Builder
    public CreatorEntity(String name) {
        this.name = name;
    }

    @PrePersist
    private void generateCreatorId() {
        this.creatorId = "creator-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
