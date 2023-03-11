package com.example.demo.src.data.entity;

import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    @Query(value = "select * from diary where delete_flag = false and diarybook_id = :diarybookId", nativeQuery = true)
    List<Diary> findByDiarybookId(@Param(value="diarybookId") int diarybookId);
}
