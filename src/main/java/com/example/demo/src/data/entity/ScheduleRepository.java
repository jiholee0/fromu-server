package com.example.demo.src.data.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = "SELECT * FROM schedule where couple_id = (:coupleId) and substr(date, 1, 6) = (:month)",nativeQuery = true)
    List<Schedule> findAllByCoupleIdAndMonth(@Param(value="coupleId") int coupleId, @Param(value = "month") String month);
    @Query(value = "SELECT * FROM schedule where couple_id = (:coupleId) and substr(date, 1, 6) = (:month) and substr(date, 7, 2) = (:date)",nativeQuery = true)
    List<Schedule> findAllByCoupleIdAndMonthAndDate(@Param(value="coupleId") int coupleId, @Param(value = "month") String month, @Param(value = "date") String date);
    @Query(value = "SELECT substr(date,7,2) FROM schedule where couple_id = (:coupleId) and substr(date, 1, 6) = (:month)",nativeQuery = true)
    List<String> getDateByCoupleIdAndMonth(@Param(value="coupleId") int coupleId, @Param(value = "month") String month);
    @Query(value = "SELECT * FROM schedule where couple_id = (:coupleId) and substr(date, 5, 2) = substr((:month), 5, 2)",nativeQuery = true)
    List<Schedule> findAnniversaryByCoupleIdAndMonth(@Param(value="coupleId") int coupleId, @Param(value = "month") String month);
    @Query(value = "SELECT * FROM schedule where couple_id = (:coupleId) and substr(date, 5, 2) = substr((:month), 5, 2) and substr(date, 7, 2) = (:date)",nativeQuery = true)
    List<Schedule> findAnniversaryByCoupleIdAndMonthAndDate(@Param(value="coupleId") int coupleId, @Param(value = "month") String month, @Param(value = "date") String date);
    @Query(value = "SELECT substr(date,7,2) FROM schedule where couple_id = (:coupleId) and substr(date, 5, 2) = substr((:month), 5, 2)",nativeQuery = true)
    List<String> getAnniversaryDateByCoupleIdAndMonthAndDate(@Param(value="coupleId") int coupleId, @Param(value = "month") String month);
}
