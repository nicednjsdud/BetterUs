package com.betterus.domain.gudok.repository;

import com.betterus.domain.gudok.domain.Gudok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GudokRepository extends JpaRepository<Gudok, Long> {
    int findByMemberId(Long id);

    @Query("select g from Gudok g left join fetch g.member m where g.authorId = :authorId and m.id = :memberId" )
    Gudok findGudokTure(@Param("authorId") Long authorId,@Param("memberId") Long memberId);

    Gudok findByAuthorIdAndMemberId(Long authorId, Long sessionMemberId);
}
