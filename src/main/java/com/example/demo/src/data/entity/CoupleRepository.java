package com.example.demo.src.data.entity;

import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Integer> {
    @NotNull List<Couple> findAll();
    Optional<Couple> findByUserId1(int userId);
    Optional<Couple> findByUserId2(int userId);
    @Where(clause = "delete_flag = false")
    Optional<Couple> findByUserId1OrUserId2(int userId1, int userId2);
    @Where(clause = "delete_flag = false")
    Optional<Couple> findByMailboxName(String str);
}
