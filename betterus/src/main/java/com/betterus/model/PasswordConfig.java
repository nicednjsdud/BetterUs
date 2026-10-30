package com.betterus.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 비밀번호 해시 정책 (개발계획 Part1 §4 보안 개선 과제 — BCrypt 적용)
 *
 * - 새 비밀번호는 BCrypt 해시로 저장한다.
 * - 기존에 평문으로 저장된 값도 로그인·확인 단계에서 계속 통과시키기 위해
 *   {@link #matches(PasswordEncoder, String, String)} 에서 해시/평문을 함께 판정한다.
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 저장된 값이 BCrypt 해시면 대조로, 평문(기존 데이터)이면 직접 비교로 확인한다. */
    public static boolean matches(PasswordEncoder encoder, String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) return false;
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            return encoder.matches(rawPassword, storedPassword);
        }
        return rawPassword.equals(storedPassword);
    }
}
