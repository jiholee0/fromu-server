package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.diarybook.PostDiarybookReq;
import com.example.demo.src.data.dto.diarybook.PostDiarybookRes;
import com.example.demo.src.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;


@Repository
public class DiarybookDao {
    @Autowired
    DiarybookRepository diarybookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CoupleRepository coupleRepository;

    @Transactional
    public int createDiarybook(int userId, PostDiarybookReq postDiarybookReq) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try{
            Diarybook diarybook = postDiarybookReq.toEntity(userId, couple.get().getCoupleId());
            diarybookRepository.save(diarybook);
            return diarybook.getDiarybookId();
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyDiarybookCover(int userId, int coverNum) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            diarybook.get().modifyDiarybookCover(coverNum);
            return diarybook.get().diarybookId;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyDiarybookName(int userId, String name) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findByCoupleId(couple.get().getCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            diarybook.get().modifyDiarybookName(name);
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
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            return diarybook.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Diarybook getDiarybookByDiarybookId(int diarybookId) throws BaseException {
        Optional<Diarybook> diarybook = Optional.of(diarybookRepository.findById(diarybookId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            return diarybook.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
