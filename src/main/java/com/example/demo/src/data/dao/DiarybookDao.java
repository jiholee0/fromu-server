package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.diarybook.GetFirstPageRes;
import com.example.demo.src.data.dto.diarybook.PostDiarybookReq;
import com.example.demo.src.data.dto.diarybook.PostDiarybookRes;
import com.example.demo.src.data.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import software.amazon.ion.Timestamp;

import javax.transaction.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class DiarybookDao {
    @Autowired
    DiarybookRepository diarybookRepository;
    @Autowired
    CoupleRepository coupleRepository;
    @Autowired
    NoticeRepository noticeRepository;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date date = new Date();


    @Transactional
    public int createDiarybook(int userId, PostDiarybookReq postDiarybookReq) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> checkDiarybook = diarybookRepository.findByCoupleId(couple.get().getCoupleId());
        if(checkDiarybook.isPresent()) throw new BaseException(POST_DIARYBOOKS_EXISTS_COUPLE);
        try{
            Diarybook diarybook = postDiarybookReq.toEntity(userId, couple.get().getCoupleId());
            diarybookRepository.save(diarybook);
            date = new Date();
            Notice notice = new Notice(couple.get().getCoupleId(),"우리만의 교환일기, "+postDiarybookReq.getName()+"(이)가 만들어졌어!", date);
            noticeRepository.save(notice);
            return diarybook.getDiarybookId();
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyDiarybookCover(int userId, int coverNum) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            diarybook.get().modifyDiarybookCover(coverNum);
            date = new Date();
            Notice notice = new Notice(couple.get().getCoupleId(),diarybook.get().getName()+"의 표지가 편집되었어!", date);
            noticeRepository.save(notice);
            return diarybook.get().diarybookId;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyDiarybookName(int userId, String name) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            diarybook.get().modifyDiarybookName(name);
            date = new Date();
            Notice notice = new Notice(couple.get().getCoupleId(),diarybook.get().getName()+"의 이름이 편집되었어!", date);
            noticeRepository.save(notice);
            return diarybook.get().getDiarybookId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int uploadDiarybookImage(int userId, String imageUrl) throws BaseException{
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            diarybook.get().uploadDiarybookImage(imageUrl);
            date = new Date();
            Notice notice = new Notice(couple.get().getCoupleId(),diarybook.get().getName()+"의 내지가 편집되었어!", date);
            noticeRepository.save(notice);
            return diarybook.get().getDiarybookId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Diarybook> getDiarybooks() throws BaseException { return diarybookRepository.findAll(); }

    @Transactional
    public Diarybook getDiarybookByCoupleId(int coupleId) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(coupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            return diarybook.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Diarybook getDiarybookByUserId(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            return diarybook.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public boolean isExistDiarybook(int coupleId){
        Optional<Diarybook> diarybook = diarybookRepository.findByCoupleId(coupleId);
        return diarybook.isPresent();
    }

    @Transactional
    public Diarybook getDiarybookByDiarybookId(int diarybookId) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findById(diarybookId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            return diarybook.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int passDiarybook(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        if(!diarybook.get().isWriteFlag()){
            throw new BaseException(PATCH_DIARYBOOKS_NOT_WRITE_DIARY);
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 3);
        Date turnTime = new Date(cal.getTimeInMillis());
        int partnerId;
        if (couple.get().getUserId1()==userId) {partnerId=couple.get().getUserId2();}
        else {partnerId = couple.get().getUserId1();}
        diarybook.get().passDiary(partnerId,turnTime); // 작성할 차례인 userId, 작성 가능 시간
        return diarybook.get().getDiarybookId();
    }

    @Transactional
    public GetFirstPageRes getFirstPage(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        try {
            return new GetFirstPageRes(
                    diarybook.get().getDiarybookId(),
                    diarybook.get().getName(),
                    diarybook.get().getImageUrl(),
                    diarybook.get().isWriteFlag());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
