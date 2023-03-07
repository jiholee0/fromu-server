package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.diary.DiaryInfo;
import com.example.demo.src.data.dto.diary.DiaryReq;
import com.example.demo.src.data.dto.diary.DiaryRes;
import com.example.demo.src.data.dto.diary.GetDiaryListByMonthRes;
import com.example.demo.src.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

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
        if(diarybook.get().isWriteFlag()){
            throw new BaseException(POST_DIARIES_ALREADY_WRITE);
        }
        if(diarybook.get().getTurnUserId()!=userId){
            throw new BaseException(POST_DIARIES_NOT_TURN);
        }
        if(diarybook.get().getTurnTime()!=null && date.before(diarybook.get().getTurnTime()) ){
            throw new BaseException(POST_DIARIES_NOT_YET);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Diary diary = postDiaryReq.toEntity(diarybook.get().getDiarybookId(),userId, imageUrl, sdf.format(date));
            diaryRepository.save(diary);
            diarybook.get().writeDiary();
            return diary.getDiaryId();
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public DiaryRes modifyDiary(int userId, int diaryId, DiaryReq postDiaryReq, String imageUrl) throws BaseException {
        Optional<Diary> diary = Optional.of(diaryRepository.findById(diaryId)).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARY)
        );
        if(diary.get().getUserId()!=userId){
            throw new BaseException(INVALID_USER_JWT);
        }
        if(!imageUrl.equals("")){
            diary.get().modifyDiaryAll(postDiaryReq.getContent(),imageUrl, postDiaryReq.getWeather());
        } else{
            diary.get().modifyDiaryExceptImage(postDiaryReq.getContent(), postDiaryReq.getWeather());
        }
        return new DiaryRes(diaryId, diary.get().getContent(), diary.get().getImageUrl(), diary.get().getWeather(), diary.get().getDate());
    }

    @Transactional
    public DiaryRes getDiaryByDiaryId(int diaryId) throws BaseException {
        Optional<Diary> diary = Optional.of(diaryRepository.findById(diaryId)).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARY)
        );
        return new DiaryRes(diaryId, diary.get().getContent(), diary.get().getImageUrl(), diary.get().getWeather(), diary.get().getDate());
    }

    @Transactional
    public List<String> getMonthList(int diarybookId) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findById(diarybookId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        List<Diary> diaryList = diaryRepository.findByDiarybookId(diarybookId);
        Set<String> monthSet = new HashSet<>();
        for(Diary diary : diaryList){
            monthSet.add(diary.date.substring(0,6));
        }
        List<String> monthList = new ArrayList<>(monthSet);
        Collections.sort(monthList);
        Collections.reverse(monthList);
        return monthList;
    }

    @Transactional
    public GetDiaryListByMonthRes getDiaryListByMonth(int diarybookId, String month) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findById(diarybookId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        List<Diary> diaryList = diaryRepository.findByDiarybookId(diarybookId);
        List<DiaryInfo> diaryInfoList = new ArrayList<>();
        for(Diary diary : diaryList){
            if(diary.getDate().substring(0,6).equals(month)){
                diaryInfoList.add(new DiaryInfo(diary.getDiaryId(),diary.getContent(),diary.getDate()));
            }
        }
        return new GetDiaryListByMonthRes(month,diaryInfoList);
    }

    @Transactional
    public List<Integer> getDiaries(int diarybookId) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findById(diarybookId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        List<Diary> diaryList = diaryRepository.findByDiarybookId(diarybookId);
        List<Integer> diaryIdList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryIdList.add(diary.getDiaryId());
        }
        Collections.reverse(diaryIdList);
        return diaryIdList;
    }

    @Transactional
    public int deleteDiary(int userId, int diaryId) throws BaseException {
        Optional<Diary> diary = Optional.of(diaryRepository.findById(diaryId)).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARY)
        );
        if(diary.get().getUserId()!=userId){
            throw new BaseException(INVALID_USER_JWT);
        }
        try {
            diaryRepository.deleteById(diaryId);
            return diary.get().getDiaryId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

