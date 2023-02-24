package com.example.demo.src.data.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Integer> {
    @NotNull List<Couple> findAll();
    Optional<Couple> findByUser1Id(int userId);
    Optional<Couple> findByUser2Id(int userId);
}
