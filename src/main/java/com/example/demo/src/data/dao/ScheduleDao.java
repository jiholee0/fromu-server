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
        Optional<User> user1 = Optional.of(userRepository.findById(couple.get().getUserId1()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<User> user2 = Optional.of(userRepository.findById(couple.get().getUserId2()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        List<Schedule> scheduleList = scheduleRepository.findAllByCoupleIdAndMonth(couple.get().getCoupleId(), month);
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
        try {
            // 기념일
            if(couple.get().getFirstMetDay() != null && couple.get().getFirstMetDay().substring(4,6).equals(month.substring(4,6))){
                int year = (Integer.parseInt(month) - Integer.parseInt(couple.get().getFirstMetDay().substring(0,6)))/100;
                if(year == 0){
                    scheduleRes.add(new ScheduleRes(
                            0,
                            "우리가 처음 만난 날",
                            couple.get().getFirstMetDay(),
                            0,
                            "우리",
                            false
                    ));
                }
                else if(year > 0){
                    scheduleRes.add(new ScheduleRes(
                            0,
                            "우리의 "+year+"주년",
                            couple.get().getFirstMetDay(),
                            0,
                            "우리",
                            false
                    ));
                }

            }
            // 생일
            if(user1.get().getBirthday().substring(4,6).equals(month.substring(4,6))){
                scheduleRes.add(new ScheduleRes(
                        0,
                        user1.get().getNickname()+"의 생일",
                        user1.get().getBirthday(),
                        user1.get().getUserId(),
                        user1.get().getNickname(),
                        false
                ));
            }
            if(user2.get().getBirthday().substring(4,6).equals(month.substring(4,6))){
                scheduleRes.add(new ScheduleRes(
                        0,
                        user2.get().getNickname()+"의 생일",
                        user2.get().getBirthday(),
                        user2.get().getUserId(),
                        user2.get().getNickname(),
                        false
                ));
            }
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
        Optional<User> user1 = Optional.of(userRepository.findById(couple.get().getUserId1()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<User> user2 = Optional.of(userRepository.findById(couple.get().getUserId2()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        List<Schedule> scheduleList = scheduleRepository.findAllByCoupleIdAndMonthAndDate(couple.get().getCoupleId(), month, date);
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
        try {
            // 기념일
            if(couple.get().getFirstMetDay() != null && couple.get().getFirstMetDay().substring(4,8).equals(month.substring(4,6)+date)){
                int year = (Integer.parseInt(month) - Integer.parseInt(couple.get().getFirstMetDay().substring(0,6)))/100;
                if(year == 0){
                    scheduleRes.add(new ScheduleRes(
                            0,
                            "우리가 처음 만난 날",
                            couple.get().getFirstMetDay(),
                            0,
                            "우리",
                            false
                    ));
                }
                else if(year > 0){
                    scheduleRes.add(new ScheduleRes(
                            0,
                            "우리의 "+year+"주년",
                            couple.get().getFirstMetDay(),
                            0,
                            "우리",
                            false
                    ));
                }

            }
            // 생일
            if(user1.get().getBirthday().substring(4,8).equals(month.substring(4,6)+date)){
                scheduleRes.add(new ScheduleRes(
                        0,
                        user1.get().getNickname()+"의 생일",
                        user1.get().getBirthday(),
                        user1.get().getUserId(),
                        user1.get().getNickname(),
                        false
                ));
            }
            if(user2.get().getBirthday().substring(4,8).equals(month.substring(4,6)+date)){
                scheduleRes.add(new ScheduleRes(
                        0,
                        user2.get().getNickname()+"의 생일",
                        user2.get().getBirthday(),
                        user2.get().getUserId(),
                        user2.get().getNickname(),
                        false
                ));
            }
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
        Optional<User> user1 = Optional.of(userRepository.findById(couple.get().getUserId1()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<User> user2 = Optional.of(userRepository.findById(couple.get().getUserId2()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            List<String> dateList = scheduleRepository.getDateByCoupleIdAndMonth(couple.get().getCoupleId(), month);
            Set<Integer> dateSet = new HashSet<>();
            for(String date : dateList){
                dateSet.add(Integer.parseInt(date));
            }
            // 기념일
            if(couple.get().getFirstMetDay() != null && couple.get().getFirstMetDay().substring(4,6).equals(month.substring(4,6))){
                int year = (Integer.parseInt(month) - Integer.parseInt(couple.get().getFirstMetDay().substring(0,6)))/100;
                if(year >= 0){
                    dateSet.add(Integer.parseInt(couple.get().getFirstMetDay().substring(6,8)));
                }
            }
            // 생일
            if(user1.get().getBirthday().substring(4,6).equals(month.substring(4,6))){
                dateSet.add(Integer.parseInt(user1.get().getBirthday().substring(6,8)));
            }
            if(user2.get().getBirthday().substring(4,6).equals(month.substring(4,6))){
                dateSet.add(Integer.parseInt(user2.get().getBirthday().substring(6,8)));
            }
            return new ArrayList<>(dateSet);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
