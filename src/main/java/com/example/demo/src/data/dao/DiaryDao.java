package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.diary.*;
import com.example.demo.src.data.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    @Autowired
    UserRepository userRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date date = new Date();

    @Transactional
    public int createDiary(int userId, DiaryReq postDiaryReq, String imageUrl) throws BaseException {
        date = new Date();
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
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
            Notice notice = new Notice(couple.get().getCoupleId(),user.get().getNickname()+"(이)가 일기를 작성했어!", date);
            noticeRepository.save(notice);
            return diary.getDiaryId();
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyDiary(int userId, int diaryId, DiaryReq postDiaryReq, String imageUrl) throws BaseException {
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
    }

    @Transactional
    public DiaryRes getDiaryByDiaryId(int diaryId) throws BaseException {
        Optional<Diary> diary = Optional.of(diaryRepository.findById(diaryId)).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARY)
        );
        Optional<User> user = Optional.of(userRepository.findById(diary.get().getUserId())).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        );
        String writerNickname = user.get().getNickname();
        String date = diary.get().getDate();

        LocalDate localDate = LocalDate.of(
                Integer.parseInt(date.substring(0,4)),
                Integer.parseInt(date.substring(4,6)),
                Integer.parseInt(date.substring(6,8)));
        return new DiaryRes(diaryId, writerNickname, diary.get().getContent(), diary.get().getImageUrl(), diary.get().getWeather(), date,localDate.getDayOfWeek().getValue());
    }

    @Transactional
    public DiaryResWithUserId getDiaryWithUserIdByDiaryId(int diaryId) throws BaseException {
        Optional<Diary> diary = Optional.of(diaryRepository.findById(diaryId)).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARY)
        );
        Optional<User> user = Optional.of(userRepository.findById(diary.get().getUserId())).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        );
        String writerNickname = user.get().getNickname();
        String date = diary.get().getDate();

        LocalDate localDate = LocalDate.of(
                Integer.parseInt(date.substring(0,4)),
                Integer.parseInt(date.substring(4,6)),
                Integer.parseInt(date.substring(6,8)));
        return new DiaryResWithUserId(diaryId, writerNickname, diary.get().getUserId(), diary.get().getContent(), diary.get().getImageUrl(), diary.get().getWeather(), date,localDate.getDayOfWeek().getValue());
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
        // Collections.reverse(diaryIdList); // 내림차순
        return diaryIdList;
    }

    @Transactional
    public List<GetDiariesWithUserId> getDiariesWithUserId(int diarybookId) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findById(diarybookId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARYBOOK)
        ));
        List<Diary> diaryList = diaryRepository.findByDiarybookId(diarybookId);
        List<GetDiariesWithUserId> diaryIdList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryIdList.add(new GetDiariesWithUserId(diary.getDiaryId(),diary.getUserId()));
        }
        // Collections.reverse(diaryIdList); // 내림차순
        return diaryIdList;
    }


    @Transactional
    public void deleteDiary(int userId, int diaryId) throws BaseException {
        Optional<Diary> diary = Optional.of(diaryRepository.findById(diaryId)).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_DIARY)
        );
        if(diary.get().getUserId()!=userId){
            throw new BaseException(INVALID_USER_JWT);
        }
        try {
            diaryRepository.deleteById(diaryId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

