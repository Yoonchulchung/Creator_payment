package com.example.demo.global.infrastructure.auth.jwt;

import com.example.demo.global.infrastructure.auth.security.CustomUserDetails;
import com.example.demo.user.domain.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            String body = request.getReader().lines().reduce("", (a, b) -> a + b);
            JsonNode json = new ObjectMapper().readTree(body);

            String username = json.get("username").asText();
            String password = json.get("password").asText();

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long userId = principal.getUserId();
        Role role = principal.getUserRole();

        long accessExpMs = 1000L * 60 * 30;       // 30분
        long refreshExpMs = 1000L * 60 * 60 * 24 * 14; // 14일

        String accessToken = jwtUtil.createJwt(userId, role, accessExpMs);
        String refreshToken = jwtUtil.createRefreshToken(userId, refreshExpMs);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        try {
            response.getWriter().write("""
                    {"accessToken":"%s","refreshToken":"%s"}
                    """.formatted(accessToken, refreshToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        try {
            response.getWriter().write("""
                    {"error":"UNAUTHORIZED","message":"아이디 또는 비밀번호가 올바르지 않습니다."}
                    """);
        } catch (Exception ignored) {
        }
    }
}
