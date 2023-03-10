package com.example.demo.src.data.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StampStatusRepository extends JpaRepository<StampStatus, Integer> {
    Optional<StampStatus> findByCoupleId(int coupleId);

}
