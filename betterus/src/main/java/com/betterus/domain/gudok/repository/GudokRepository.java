package com.betterus.domain.gudok.repository;

import com.betterus.domain.gudok.domain.Gudok;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GudokRepository extends JpaRepository<Gudok, Long> {
    int findByMemberId(Long id);
}
