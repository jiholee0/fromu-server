package com.example.demo.src.data.entity;

import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Integer> {
    @NotNull List<Couple> findAll();
    Optional<Couple> findByUserId1(int userId);
    Optional<Couple> findByUserId2(int userId);
    Optional<Couple> findByUserId1OrUserId2(int userId1, int userId2);
    Optional<Couple> findByMailboxName(String str);

    @Query(value = "SELECT * FROM couple where couple_id not in(:coupleId) order by RAND(now()) limit 1",nativeQuery = true)
    Couple findRandomCouple(@Param(value = "coupleId") int coupleId);
}
