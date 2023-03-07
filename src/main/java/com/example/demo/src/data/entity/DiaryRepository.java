package com.example.demo.src.data.entity;

import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    @Where(clause = "delete_flag = false")
    List<Diary> findByDiarybookId(int diarybookId);
}
