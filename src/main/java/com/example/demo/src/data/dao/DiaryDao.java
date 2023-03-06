package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.diary.DiaryReq;
import com.example.demo.src.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class DiaryDao {
    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    DiarybookRepository diarybookRepository;
    @Autowired
    CoupleRepository coupleRepository;

    @Transactional
    public int createDiary(int userId, DiaryReq postDiaryReq, String imageUrl) throws BaseException {
        Date date = new Date();
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        if(diarybook.get().getTurnUserId()!=userId){
            throw new BaseException(POST_DIARIES_NOT_TURN);
        }
        if(diarybook.get().getTurnTime()!=null && date.before(diarybook.get().getTurnTime()) ){
            throw new BaseException(POST_DIARIES_NOT_YET);
        }
        try {
            Diary diary = postDiaryReq.toEntity(diarybook.get().getDiarybookId(),userId, imageUrl);
            diaryRepository.save(diary);
            return diary.getDiaryId();
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyDiary(int userId, int diaryId, DiaryReq postDiaryReq, String imageUrl){

    }

}

