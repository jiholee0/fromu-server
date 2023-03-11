package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.controller.PushController;
import com.example.demo.src.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.From;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository

public class ShopDao {

    @Autowired
    FromStatusRepository fromStatusRepository;
    @Autowired
    StampStatusRepository stampStatusRepository;
    @Autowired
    PushStatusRepository pushStatusRepository;
    @Autowired
    CoupleRepository coupleRepository;

    @Transactional
    public int buyStamp(int userId, int stampNum) throws BaseException{
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<FromStatus> fromStatus = Optional.of(fromStatusRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_FROM)
        ));
        if(fromStatus.get().getFromCount()<3){throw new BaseException(NOT_ENOUGH_FROM);}
        Optional<StampStatus> stampStatus = Optional.of(stampStatusRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_STAMP)
        ));
        switch(stampNum){
            case 1 : {stampStatus.get().buyStamp1();break;}
            case 2 : {stampStatus.get().buyStamp2();break;}
            case 3 : {stampStatus.get().buyStamp3();break;}
            case 4 : {stampStatus.get().buyStamp4();break;}
            case 5 : {stampStatus.get().buyStamp5();break;}
            case 6 : {stampStatus.get().buyStamp6();break;}
        }
        fromStatus.get().useFrom(3);
        return couple.get().getCoupleId();
    }

    @Transactional
    public void useStamp(int userId, int stampNum) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<StampStatus> stampStatus = Optional.of(stampStatusRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_STAMP)
        ));
        switch(stampNum){
            case 1 : {if(stampStatus.get().getStampCount1()<=0) {throw new BaseException(NOT_ENOUGH_STAMP);} stampStatus.get().useStamp1();break;}
            case 2 : {if(stampStatus.get().getStampCount2()<=0) {throw new BaseException(NOT_ENOUGH_STAMP);} stampStatus.get().useStamp2();break;}
            case 3 : {if(stampStatus.get().getStampCount3()<=0) {throw new BaseException(NOT_ENOUGH_STAMP);} stampStatus.get().useStamp3();break;}
            case 4 : {if(stampStatus.get().getStampCount4()<=0) {throw new BaseException(NOT_ENOUGH_STAMP);} stampStatus.get().useStamp4();break;}
            case 5 : {if(stampStatus.get().getStampCount5()<=0) {throw new BaseException(NOT_ENOUGH_STAMP);} stampStatus.get().useStamp5();break;}
            case 6 : {if(stampStatus.get().getStampCount6()<=0) {throw new BaseException(NOT_ENOUGH_STAMP);} stampStatus.get().useStamp6();break;}
        }
    }

    @Transactional
    public Map<Integer, Integer> scoreLetter(int coupleId, int getScoreCoupleId, int score) throws BaseException {
        Optional<FromStatus> fromStatusOfCouple = Optional.of(fromStatusRepository.findByCoupleId(coupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_FROM)
        ));
        Optional<FromStatus> fromStatusOfGetCouple = Optional.of(fromStatusRepository.findByCoupleId(getScoreCoupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_FROM)
        ));
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0,fromStatusOfCouple.get().getFrom(1));
        map.put(1,fromStatusOfGetCouple.get().getFrom(score));
        return map;
    }

    @Transactional
    public int getFromByPassDiarybook(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<FromStatus> fromStatus = Optional.of(fromStatusRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_FROM)
        ));
        Optional<PushStatus> pushStatus = Optional.of(pushStatusRepository.findByUserId(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_PUSH)
        ));
        fromStatus.get().getFrom(1);
        pushStatus.get().charge();
        return fromStatus.get().getFromCount();
    }

    @Transactional
    public void init(int coupleId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findById(coupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        try {
            FromStatus fromStatus = new FromStatus(coupleId,0,false);
            fromStatusRepository.save(fromStatus);
            StampStatus stampStatus = new StampStatus(coupleId,3,0,0,0,0,0);
            stampStatusRepository.save(stampStatus);
            PushStatus pushStatus1 = new PushStatus(couple.get().getUserId1(),15);
            PushStatus pushStatus2 = new PushStatus(couple.get().getUserId2(),15);
            pushStatusRepository.save(pushStatus1);
            pushStatusRepository.save(pushStatus2);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public boolean push(int userId) throws BaseException {
        Optional<PushStatus> pushStatus = Optional.of(pushStatusRepository.findByUserId(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_PUSH)
        ));
        if(pushStatus.get().getPushCount()<=0){
            pushStatus.get().startCharge();
            //charge(userId);
            chargeTest(userId);
            return false;
        } else{
            pushStatus.get().push();
            return true;
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000) // 12시간 후에 실행
    public void charge(int userId) throws BaseException {
        Optional<PushStatus> pushStatus = Optional.of(pushStatusRepository.findByUserId(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_PUSH)
        ));
        LocalDateTime createdTime = pushStatus.get().getChargeTime().toInstant()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();;
        LocalDateTime scheduledTime = createdTime.plusHours(12);
        LocalDateTime currentTime = LocalDateTime.now();
        if (scheduledTime.isBefore(currentTime)) {
            pushStatus.get().charge();
        }
    }

    @Transactional
    @Scheduled(fixedDelay = (1/60) * 60 * 60 * 1000) // 1분 후에 실행
    public void chargeTest(int userId) throws BaseException {
        Optional<PushStatus> pushStatus = Optional.of(pushStatusRepository.findByUserId(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_PUSH)
        ));
        LocalDateTime createdTime = pushStatus.get().getChargeTime().toInstant()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();;
        LocalDateTime scheduledTime = createdTime.plusMinutes(1);
        LocalDateTime currentTime = LocalDateTime.now();
        if (scheduledTime.isBefore(currentTime)) {
            pushStatus.get().charge();
        }
    }

    @Transactional
    public int getFromByUserId(int userId) throws BaseException{
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<FromStatus> fromStatus = Optional.of(fromStatusRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_FROM)
        ));
        return fromStatus.get().getFromCount();
    }
}
