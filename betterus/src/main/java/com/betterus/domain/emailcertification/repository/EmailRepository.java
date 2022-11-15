package com.betterus.domain.emailcertification.repository;

import com.betterus.domain.emailcertification.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Email findByEmail(String email);

    Email findByAuthCode(String authCode);
}
