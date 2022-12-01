package com.betterus.domain.email.repository;

import com.betterus.domain.email.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Email findByEmail(String email);

    Email findByAuthCode(String authCode);
}
