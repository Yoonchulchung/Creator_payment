package com.example.demo.global.infrastructure.auth.jwt;

import com.example.demo.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class MasterTokenGenerator implements ApplicationRunner {

    private final JWTUtil jwtUtil;
    private static final long EXPIRE_MS = 1000L * 60 * 60 * 24 * 365; // 1년

    @Override
    public void run(ApplicationArguments args) {
        String creatorToken = jwtUtil.createJwt(1L, Role.CREATOR, EXPIRE_MS);
        String adminToken   = jwtUtil.createJwt(11L, Role.ADMIN, EXPIRE_MS);

        log.info("\n" +
                "=== MASTER TOKENS (local only) ===\n" +
                "[CREATOR] creator-1 (id=1)\n{}\n\n" +
                "[ADMIN]   admin (id=11)\n{}\n" +
                "===================================",
                creatorToken, adminToken);
    }
}
