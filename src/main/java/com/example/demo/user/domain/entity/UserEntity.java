package com.example.demo.user.domain.entity;

import com.example.demo.global.entity.BaseEntity;
import com.example.demo.user.domain.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public UserEntity(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // JWTFilter 전용 — DB 조회 없이 인증 객체 생성
    public static UserEntity forAuthentication(Long id, Role role) {
        UserEntity entity = new UserEntity();
        entity.id = id;
        entity.role = role;
        return entity;
    }
}
