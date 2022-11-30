package com.betterus.domain.jjim.repository;

import com.betterus.domain.jjim.domain.Jjim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JjimRepository extends JpaRepository<Jjim, Long> {
    Jjim findByArticleIdAndMemberId(Long articleId, Long id);
}
