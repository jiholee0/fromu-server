package com.example.demo.src.data.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @NotNull List<Notice> findByCoupleId(int coupleId);
}
