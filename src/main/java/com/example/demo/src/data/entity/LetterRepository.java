package com.example.demo.src.data.entity;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LetterRepository extends JpaRepository<Letter, Integer> {
    @Where(clause = "report_flag = false")
    Optional<Letter> findByRefLetterId(int refLetterId);
    @Where(clause = "report_flag = false")
    List<Letter> findBySendCoupleId(int sendCoupleId);
    @Where(clause = "report_flag = false")
    List<Letter> findByReceiveCoupleId(int receiveCoupleId);

}
