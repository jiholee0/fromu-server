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

    Optional<Letter> findByRefLetterId(int refLetterId);
    @Query(value = "select * from letter where report_flag = false and send_couple_id = :sendCoupleId", nativeQuery = true)
    List<Letter> findBySendCoupleId(@Param(value="sendCoupleId") int sendCoupleId);
    @Query(value = "select * from letter where report_flag = false and receive_couple_id = :receiveCoupleId", nativeQuery = true)
    List<Letter> findByReceiveCoupleId(@Param(value="receiveCoupleId") int receiveCoupleId);

}
