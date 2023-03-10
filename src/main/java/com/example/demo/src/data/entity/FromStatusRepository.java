package com.example.demo.src.data.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FromStatusRepository extends JpaRepository<FromStatus, Integer> {
    Optional<FromStatus> findByCoupleId(int coupleId);

}
