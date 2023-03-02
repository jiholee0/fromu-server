package com.example.demo.src.data.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiarybookRepository extends JpaRepository<Diarybook, Integer> {
    @NotNull List<Diarybook> findAll();
    Optional<Diarybook> findById(int diarybookId);
    Optional<Diarybook> findByCoupleId(int coupleId);
}
