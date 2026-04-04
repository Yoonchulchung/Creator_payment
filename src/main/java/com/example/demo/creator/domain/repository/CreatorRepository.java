package com.example.demo.creator.domain.repository;

import com.example.demo.creator.domain.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreatorRepository extends JpaRepository<CreatorEntity, Long> {

    Optional<CreatorEntity> findByName(String name);

    List<CreatorEntity> findAllByDeletedAtIsNull();
}
