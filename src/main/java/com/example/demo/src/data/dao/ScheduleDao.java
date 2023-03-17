package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.dto.schedule.ScheduleReq;
import com.example.demo.src.data.dto.schedule.ScheduleRes;
import com.example.demo.src.data.dto.view.NoticeViewRes;
import com.example.demo.src.data.entity.*;
import com.example.demo.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.*;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class ScheduleDao {
    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date date = new Date();

    @Transactional
    public int createSchedule(int userId, ScheduleReq scheduleReq) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        date = new Date();
        Schedule schedule = scheduleReq.toEntity(userId, couple.get().getCoupleId());
        scheduleRepository.save(schedule);
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            Notice notice = new Notice(couple.get().getCoupleId(),user.get().getNickname()+"의 새로운 일정이 등록되었어!", date);
            noticeRepository.save(notice);
            return schedule.getScheduleId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<ScheduleRes> getSchedulesByMonth(int userId, String month) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        List<Schedule> scheduleList = scheduleRepository.findAllByCoupleIdAndMonth(couple.get().getCoupleId(), month);
        List<ScheduleRes> scheduleRes = new ArrayList<>();
        List<Schedule> autoList = scheduleRepository.findAnniversaryByCoupleIdAndMonth(couple.get().getCoupleId(), month);
        for(Schedule schedule : scheduleList){
            Optional<User> user = Optional.of(userRepository.findById(schedule.getUserId()).orElseThrow(
                    () -> new BaseException(NOT_EXIST_DATA)
            ));
            scheduleRes.add(new ScheduleRes(
                    schedule.getScheduleId(),
                    schedule.getContent(),
                    schedule.getDate(),
                    schedule.getUserId(),
                    user.get().getNickname(),
                    true
            ));
        }
        for(Schedule anniversary : autoList){
            if(anniversary.getUserId() == 0){ // 기념일
                int year = (Integer.parseInt(month) - Integer.parseInt(anniversary.getDate().substring(0,6)))/100;
                scheduleRes.add(new ScheduleRes(
                        anniversary.getScheduleId(),
                        "우리의 "+year+"주년",
                        anniversary.getDate(),
                        0,
                        "우리",
                        false
                ));
            }
            else {
                Optional<User> user = Optional.of(userRepository.findById(anniversary.getUserId()).orElseThrow(
                        () -> new BaseException(NOT_EXIST_DATA)
                ));
                scheduleRes.add(new ScheduleRes(
                        anniversary.getScheduleId(),
                        user.get().getNickname()+"의 생일",
                        anniversary.getDate(),
                        user.get().getUserId(),
                        user.get().getNickname(),
                        false
                ));
            }
        }
        try {
            scheduleRes.sort(ScheduleRes.getComparatorByDate());
            return scheduleRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<ScheduleRes> getSchedulesByDate(int userId, String month, String date) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));

        List<Schedule> scheduleList = scheduleRepository.findAllByCoupleIdAndMonthAndDate(couple.get().getCoupleId(), month, date);
        List<Schedule> autoList = scheduleRepository.findAnniversaryByCoupleIdAndMonthAndDate(couple.get().getCoupleId(), month, date);
        List<ScheduleRes> scheduleRes = new ArrayList<>();
        for(Schedule schedule : scheduleList){
            Optional<User> user = Optional.of(userRepository.findById(schedule.getUserId()).orElseThrow(
                    () -> new BaseException(NOT_EXIST_DATA)
            ));
            scheduleRes.add(new ScheduleRes(
                    schedule.getScheduleId(),
                    schedule.getContent(),
                    schedule.getDate(),
                    schedule.getUserId(),
                    user.get().getNickname(),
                    true
            ));
        }
        for(Schedule anniversary : autoList){
            if(anniversary.getUserId() == 0){ // 기념일
                int year = (Integer.parseInt(month) - Integer.parseInt(anniversary.getDate().substring(0,6)))/100;
                scheduleRes.add(new ScheduleRes(
                        anniversary.getScheduleId(),
                        "우리의 "+year+"주년",
                        anniversary.getDate(),
                        0,
                        "우리",
                        false
                ));
            }
            else { // 생일
                Optional<User> user = Optional.of(userRepository.findById(anniversary.getUserId()).orElseThrow(
                        () -> new BaseException(NOT_EXIST_DATA)
                ));
                scheduleRes.add(new ScheduleRes(
                        anniversary.getScheduleId(),
                        user.get().getNickname()+"의 생일",
                        anniversary.getDate(),
                        user.get().getUserId(),
                        user.get().getNickname(),
                        false
                ));
            }
        }
        try {
            scheduleRes.sort(ScheduleRes.getComparatorByDate());
            return scheduleRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Integer> getDateListByMonth(int userId, String month) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        try {
            List<String> dateList = scheduleRepository.getDateByCoupleIdAndMonth(couple.get().getCoupleId(), month);
            List<String> anniversaryList = scheduleRepository.getAnniversaryDateByCoupleIdAndMonthAndDate(couple.get().getCoupleId(), month);
            Set<Integer> dateSet = new HashSet<>();
            for(String date : dateList){
                dateSet.add(Integer.parseInt(date));
            }
            for(String anniversary : anniversaryList){
                dateSet.add(Integer.parseInt(anniversary));
            }
            return new ArrayList<>(dateSet);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void init(int coupleId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findById(coupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<User> user1 = Optional.of(userRepository.findById(couple.get().getUserId1()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<User> user2 = Optional.of(userRepository.findById(couple.get().getUserId2()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        ScheduleReq scheduleReq = new ScheduleReq(user1.get().getNickname()+"의 생일",user1.get().getBirthday());
        Schedule schedule = scheduleReq.toEntity(user1.get().getUserId(),coupleId);
        scheduleRepository.save(schedule);
        scheduleReq = new ScheduleReq(user2.get().getNickname()+"의 생일",user2.get().getBirthday());
        schedule = scheduleReq.toEntity(user2.get().getUserId(),coupleId);
        scheduleRepository.save(schedule);
        scheduleReq = new ScheduleReq("우리가 처음 만난 날",couple.get().getFirstMetDay());
        schedule = scheduleReq.toEntity(0,coupleId);
        scheduleRepository.save(schedule);
    }
}
